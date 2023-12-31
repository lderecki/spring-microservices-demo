package pl.lderecki.crudservice.service;

import org.springframework.stereotype.Service;
import pl.lderecki.crudservice.DTO.SimpleEntityReadDTO;
import pl.lderecki.crudservice.DTO.SimpleEntityWriteDTO;
import pl.lderecki.crudservice.feignClient.DictClientAdapter;
import pl.lderecki.crudservice.model.SimpleEntity;
import pl.lderecki.crudservice.repo.SimpleEntityRepo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SimpleEntityService {

    private final SimpleEntityRepo repo;
    private final DictClientAdapter client;

    public SimpleEntityService(SimpleEntityRepo repo, DictClientAdapter client) {
        this.repo = repo;
        this.client = client;
    }

    public List<SimpleEntityReadDTO> findAll() {
        List<SimpleEntity> result =  repo.findAll();

        result.forEach(r -> client.translate(client.FIRST_DICT, r.getFirstDictKey()));

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
                toMap.getFirstDictKey(),
                client.translate(client.FIRST_DICT, toMap.getFirstDictKey()),
                toMap.getSecondDictKey(),
                client.translate(client.SECOND_DICT, toMap.getSecondDictKey()),
                toMap.getSomeTextData());
    }

    private SimpleEntity mapFromWriteDto(SimpleEntityWriteDTO toMap) {
        return new SimpleEntity(toMap.getId(), toMap.getFirstDictKey(),
                                toMap.getSecondDictKey(), toMap.getSomeTextData());
    }
}
