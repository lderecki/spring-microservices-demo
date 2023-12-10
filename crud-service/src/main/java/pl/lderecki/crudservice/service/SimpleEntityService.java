package pl.lderecki.crudservice.service;

import org.springframework.stereotype.Service;
import pl.lderecki.crudservice.DTO.SimpleEntityReadDTO;
import pl.lderecki.crudservice.DTO.SimpleEntityWriteDTO;
import pl.lderecki.crudservice.model.SimpleEntity;
import pl.lderecki.crudservice.repo.SimpleEntityJpaRepo;
import pl.lderecki.crudservice.repo.SimpleEntityRepo;
import pl.lderecki.crudservice.restTemplate.DictRestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimpleEntityService {

    private final SimpleEntityRepo repo;
    private final DictRestTemplate restTemplate;

    public SimpleEntityService(SimpleEntityJpaRepo repo, DictRestTemplate restTemplate) {
        this.repo = repo;
        this.restTemplate = restTemplate;
    }

    public List<SimpleEntityReadDTO> findAll() {
        List<SimpleEntity> result =  repo.findAll();

        result.forEach(r -> restTemplate.translate(restTemplate.FIRST_DICT, r.getFirstDictKey()));

        return result.stream()
                                    .map(this::mapToReadDTO)
                                    .collect(Collectors.toList());
    }

    public SimpleEntityReadDTO findById(long id) {
        Optional<SimpleEntity> optionalResult = repo.findById(id);

        return optionalResult.map(this::mapToReadDTO)
                             .orElseThrow(IllegalArgumentException::new);
    }

    public SimpleEntityReadDTO save(SimpleEntityWriteDTO toSaveDTO) {
        toSaveDTO.setId(null);

        SimpleEntity toSave = mapFromWriteDto(toSaveDTO);
        SimpleEntity saved = repo.save(toSave);

        return mapToReadDTO(saved);
    }

    public void update(SimpleEntityWriteDTO toUpdateDTO) {
        if (!repo.existsById(toUpdateDTO.getId()))
            throw new IllegalArgumentException();

        SimpleEntity toUpdate = mapFromWriteDto(toUpdateDTO);
        repo.save(toUpdate);
    }

    public void delete(long toDeleteId) {
        if (!repo.existsById(toDeleteId))
            throw new IllegalArgumentException();

        repo.deleteById(toDeleteId);
    }



    private SimpleEntityReadDTO mapToReadDTO(SimpleEntity toMap) {
        return new SimpleEntityReadDTO(toMap.getId(),
                restTemplate.translate(restTemplate.FIRST_DICT, toMap.getFirstDictKey()),
                restTemplate.translate(restTemplate.SECOND_DICT, toMap.getSecondDictKey()),
                toMap.getSomeTextData());
    }

    private SimpleEntity mapFromWriteDto(SimpleEntityWriteDTO toMap) {
        return new SimpleEntity(toMap.getId(), toMap.getFirstDictKey(),
                                toMap.getSecondDictKey(), toMap.getSomeTextData());
    }
}
