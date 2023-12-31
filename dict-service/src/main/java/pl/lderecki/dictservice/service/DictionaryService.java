package pl.lderecki.dictservice.service;

import org.springframework.stereotype.Service;
import pl.lderecki.dictservice.DTO.*;
import pl.lderecki.dictservice.model.Dict;
import pl.lderecki.dictservice.model.DictEntity;
import pl.lderecki.dictservice.repo.DictEntityRepo;
import pl.lderecki.dictservice.repo.DictRepo;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DictionaryService {

    private final DictRepo repo;

    private final DictEntityRepo dictEntityRepo;

    private final Map<String, DictRepoDTO> inMemoryRepo = new HashMap<>();

    public DictionaryService(DictRepo repo, DictEntityRepo dictEntityRepo) {
        this.repo = repo;
        this.dictEntityRepo = dictEntityRepo;
    }

    @PostConstruct
    public void inMemoryRepoInit() {
        List<Dict> dicts = repo.findAll();
        List<DictRepoDTO> mapped = dicts.stream()
                                    .map(DictRepoDTO::new)
                                    .collect(Collectors.toList());

        mapped.forEach(d -> inMemoryRepo.put(d.getDictId(), d));
    }

    private void refreshDict(String dictId) {
        inMemoryRepo.remove(dictId);

        Dict refreshedDict = repo.findByDictId(dictId);
        inMemoryRepo.put(dictId, new DictRepoDTO(refreshedDict));
    }

    private boolean existsEntityInMemoryRepo(String dictId, String dictKey) {
        Map<String, DictEntityRepoDTO> dict = inMemoryRepo.get(dictId).getEntities();

        return dict.containsKey(dictKey);
    }

    public Map<String, DictDTO> findAll() {

        Map<String, DictDTO> mapped = new HashMap<>();

        inMemoryRepo.values()
                .forEach(v -> mapped.put(
                        v.getDictId(),
                        new DictDTO(v.getDictId(), v.getDictName(),
                                v.getEntities().values()
                                        .stream()
                                        .filter(e -> !e.isDisabled())
                                        .map(DictEntityDTO::new)
                                        .collect(Collectors.toMap(DictEntityDTO::getDictValue, Function.identity()))))
                );

        return mapped;
    }

    public DictDTO findDictById(String dictId) {
        DictRepoDTO searchResult = inMemoryRepo.get(dictId);

        if (Objects.isNull(searchResult))
            throw new IllegalArgumentException("Not found");

        searchResult.setEntities(searchResult.getEntities().values().stream()
                .filter(e -> !e.isDisabled())
                .collect(Collectors.toMap(DictEntityRepoDTO::getDictValue, Function.identity()))
        );

        return new DictDTO(searchResult);
    }

    public DictEntityDTO findEntityById(String dictId, String dictKey) {
        if (!existsEntityInMemoryRepo(dictId, dictKey))
            throw new IllegalArgumentException("Not found");

        Map<String, DictEntityRepoDTO> rawDict = inMemoryRepo.get(dictId).getEntities();
        Map<String, DictEntityDTO> dict = rawDict.values().stream()
                .map(DictEntityDTO::new)
                .collect(Collectors.toMap(DictEntityDTO::getDictKey, Function.identity()));
        return dict.get(dictKey);
    }

    public DictEntityDTO saveDictEntity(DictEntityDTO entityDTO) {

        if (!inMemoryRepo.containsKey(entityDTO.getDictId()))
            throw new IllegalArgumentException("Dict not found");

        if (existsEntityInMemoryRepo(entityDTO.getDictId(), entityDTO.getDictKey()))
            throw new IllegalStateException("Non-unique entry");



        DictEntity entity = new DictEntity(entityDTO.getDictId(), entityDTO.getDictKey(),
                                           entityDTO.getDictValue(), false);

        DictEntity savedEntity = dictEntityRepo.save(entity);
        refreshDict(entityDTO.getDictId());

        return new DictEntityDTO(new DictEntityRepoDTO(savedEntity));
    }

    public void updateDictEntity(DictEntityUpdateDTO entityDTO, String dictId, String dictKey) {
        if (!inMemoryRepo.containsKey(dictId))
            throw new IllegalArgumentException("Dict not found");

        if (!existsEntityInMemoryRepo(dictId, dictKey))
            throw new IllegalArgumentException("Not found");

        DictEntity entity = new DictEntity(dictId, dictKey, entityDTO.getDictValue(), entityDTO.isDisabled());

        dictEntityRepo.save(entity);
        refreshDict(dictId);
    }
}
