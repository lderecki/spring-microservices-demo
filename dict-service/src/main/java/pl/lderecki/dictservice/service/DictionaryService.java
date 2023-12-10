package pl.lderecki.dictservice.service;

import org.springframework.stereotype.Service;
import pl.lderecki.dictservice.DTO.DictDTO;
import pl.lderecki.dictservice.DTO.DictEntityDTO;
import pl.lderecki.dictservice.DTO.DictEntityUpdateDTO;
import pl.lderecki.dictservice.model.Dict;
import pl.lderecki.dictservice.model.DictEntity;
import pl.lderecki.dictservice.repo.DictEntityJpaRepo;
import pl.lderecki.dictservice.repo.DictEntityRepo;
import pl.lderecki.dictservice.repo.DictRepo;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DictionaryService {

    private final DictRepo repo;

    private final DictEntityRepo dictEntityRepo;

    private final Map<String, DictDTO> inMemoryRepo = new HashMap<>();

    public DictionaryService(DictRepo repo, DictEntityJpaRepo dictEntityJpaRepo) {
        this.repo = repo;
        this.dictEntityRepo = dictEntityJpaRepo;
    }

    @PostConstruct
    public void inMemoryRepoInit() {
        List<Dict> dicts = repo.findAll();
        List<DictDTO> mapped = dicts.stream()
                                    .map(DictDTO::new)
                                    .collect(Collectors.toList());

        mapped.forEach(d -> inMemoryRepo.put(d.getDictId(), d));
    }

    private void refreshDict(String dictId) {
        inMemoryRepo.remove(dictId);

        Dict refreshedDict = repo.findByDictId(dictId);
        inMemoryRepo.put(dictId, new DictDTO(refreshedDict));
    }

    private boolean existsEntityInMemoryRepo(String dictId, String dictKey) {
        Map<String, DictEntityDTO> dict = inMemoryRepo.get(dictId).getEntities();

        return dict.containsKey(dictKey);
    }

    public Map<String, DictDTO> findAll() {
        return inMemoryRepo;
    }

    public DictDTO findDictById(String dictId) {
        DictDTO result = inMemoryRepo.get(dictId);

        if (Objects.isNull(result))
            throw new IllegalArgumentException("Not found");

        return result;
    }

    public DictEntityDTO findEntityById(String dictId, String dictKey) {
        Map<String, DictEntityDTO> dict = inMemoryRepo.get(dictId).getEntities();

        if (!existsEntityInMemoryRepo(dictId, dictKey))
            throw new IllegalArgumentException("Not found");

        return dict.get(dictKey);
    }

    public DictEntityDTO saveDictEntity(DictEntityDTO entityDTO) {

        if (existsEntityInMemoryRepo(entityDTO.getDictId(), entityDTO.getDictKey()))
            throw new IllegalStateException("Non-unique entry");

        DictEntity entity = new DictEntity(entityDTO.getDictId(), entityDTO.getDictKey(),
                                           entityDTO.getDictValue(), false);

        DictEntity savedEntity = dictEntityRepo.save(entity);
        refreshDict(entityDTO.getDictId());

        return new DictEntityDTO(savedEntity);
    }

    public void updateDictEntity(DictEntityUpdateDTO entityDTO, String dictId, String dictKey) {
        if (!existsEntityInMemoryRepo(dictId, dictKey))
            throw new IllegalArgumentException("Not found");

        DictEntity entity = new DictEntity(dictId, dictKey, entityDTO.getDictValue(), entityDTO.isDisabled());

        dictEntityRepo.save(entity);
        refreshDict(dictId);
    }
}
