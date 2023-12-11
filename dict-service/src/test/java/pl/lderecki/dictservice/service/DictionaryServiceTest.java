package pl.lderecki.dictservice.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lderecki.dictservice.DTO.DictDTO;
import pl.lderecki.dictservice.DTO.DictEntityDTO;
import pl.lderecki.dictservice.DTO.DictEntityUpdateDTO;
import pl.lderecki.dictservice.model.Dict;
import pl.lderecki.dictservice.model.DictEntity;
import pl.lderecki.dictservice.repo.DictEntityRepo;
import pl.lderecki.dictservice.repo.DictRepo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DictionaryServiceTest {

    @Mock
    private DictRepo repo;

    @Mock
    private DictEntityRepo dictEntityRepo;

    @InjectMocks
    private DictionaryService testClass;

    @Test
    void findAll() throws Exception {
        Field inMemoryRepoField = testClass.getClass().getDeclaredField("inMemoryRepo");
        inMemoryRepoField.setAccessible(true);

        Map<String, DictDTO> inMemRepoData = new HashMap<>();
        Map<String, DictEntityDTO> entities = new HashMap<>();

        entities.put("test_key", new DictEntityDTO("test_id", "test_key", "test_value"));
        DictDTO dict = new DictDTO("test_id", "Testowy", entities);
        inMemRepoData.put("test_id", dict);

        inMemoryRepoField.set(testClass, inMemRepoData);

        assertEquals(inMemRepoData, testClass.findAll());
    }

    @Test
    void findDictById() throws Exception {
        Field inMemoryRepoField = testClass.getClass().getDeclaredField("inMemoryRepo");
        inMemoryRepoField.setAccessible(true);

        Map<String, DictDTO> inMemRepoData = new HashMap<>();
        Map<String, DictEntityDTO> entities = new HashMap<>();
        Map<String, DictEntityDTO> entities2 = new HashMap<>();

        DictDTO dict = new DictDTO("test_id", "Testowy", null);
        DictDTO dict2 = new DictDTO("test_id2", "Testowy2", null);
        inMemRepoData.put("test_id", dict);
        inMemRepoData.put("test_id2", dict2);

        inMemoryRepoField.set(testClass, inMemRepoData);

        assertEquals(dict, testClass.findDictById("test_id"));

        assertThrows(IllegalArgumentException.class, () -> testClass.findDictById("test_id3"));
    }

    @Test
    void findEntityById() throws Exception {
        Field inMemoryRepoField = testClass.getClass().getDeclaredField("inMemoryRepo");
        inMemoryRepoField.setAccessible(true);

        Map<String, DictDTO> inMemRepoData = new HashMap<>();
        Map<String, DictEntityDTO> entities = new HashMap<>();

        DictEntityDTO entity = new DictEntityDTO("test_id", "test_key", "test_value");
        DictEntityDTO entity2 = new DictEntityDTO("test_id", "test_key2", "test_value2");
        entities.put("test_key", entity);
        entities.put("test_key2", entity2);
        DictDTO dict = new DictDTO("test_id", "Testowy", entities);
        inMemRepoData.put("test_id", dict);

        inMemoryRepoField.set(testClass, inMemRepoData);

        assertEquals(entity, testClass.findEntityById("test_id", "test_key"));

        assertThrows(IllegalArgumentException.class, () -> testClass.findEntityById("test_id", "not-existing"));

    }

    @Test
    void saveDictEntity() throws Exception {
        Field inMemoryRepoField = testClass.getClass().getDeclaredField("inMemoryRepo");
        inMemoryRepoField.setAccessible(true);

        Map<String, DictDTO> inMemRepoData = new HashMap<>();
        Map<String, DictEntityDTO> entities = new HashMap<>();

        entities.put("test_key", new DictEntityDTO("test_id", "test_key", "test_value"));
        DictDTO dict = new DictDTO("test_id", "Testowy", entities);
        inMemRepoData.put("test_id", dict);

        inMemoryRepoField.set(testClass, inMemRepoData);

        DictEntity correctEntity = new DictEntity("test_id", "test_key2", "test_value2", false);
        DictEntityDTO correctEntityDTO = new DictEntityDTO(correctEntity);

        DictEntityDTO nonUniqueEntityDTO = new DictEntityDTO("test_id", "test_key", "test_value");
        DictEntityDTO dictNotFoundEntityDTO = new DictEntityDTO("missing_id", "test_key", "test_value");

        List<DictEntity> repoResultEntities = new ArrayList<>();
        repoResultEntities.add(new DictEntity("test_id", "test_key", "test_value", false));
        repoResultEntities.add(new DictEntity("test_id", "test_key2", "test_value2", false));
        Dict repoResultDict = new Dict("test_id", "Testowy", repoResultEntities);


        when(dictEntityRepo.save(correctEntity)).thenReturn(correctEntity);
        when(repo.findByDictId("test_id")).thenReturn(repoResultDict);

        assertThrows(IllegalStateException.class, () -> testClass.saveDictEntity(nonUniqueEntityDTO));
        assertThrows(IllegalArgumentException.class, () -> testClass.saveDictEntity(dictNotFoundEntityDTO));
        assertEquals(correctEntityDTO, testClass.saveDictEntity(correctEntityDTO));
    }

    @Test
    void updateDictEntity() throws Exception{
        Field inMemoryRepoField = testClass.getClass().getDeclaredField("inMemoryRepo");
        inMemoryRepoField.setAccessible(true);

        Map<String, DictDTO> inMemRepoData = new HashMap<>();
        Map<String, DictEntityDTO> entities = new HashMap<>();

        entities.put("test_key", new DictEntityDTO("test_id", "test_key", "test_value"));
        DictDTO dict = new DictDTO("test_id", "Testowy", entities);
        inMemRepoData.put("test_id", dict);

        inMemoryRepoField.set(testClass, inMemRepoData);

        DictEntity correctEntity = new DictEntity("test_id", "test_key", "test_value", true);
        DictEntityUpdateDTO correctEntityDTO = new DictEntityUpdateDTO(correctEntity.getDictValue(), correctEntity.isDisabled());


        List<DictEntity> repoResultEntities = new ArrayList<>();
        repoResultEntities.add(new DictEntity("test_id", "test_key", "test_value", true));
        Dict repoResultDict = new Dict("test_id", "Testowy", repoResultEntities);


        when(dictEntityRepo.save(correctEntity)).thenReturn(correctEntity);
        when(repo.findByDictId("test_id")).thenReturn(repoResultDict);

        assertThrows(IllegalArgumentException.class, () -> testClass.updateDictEntity(correctEntityDTO, "test_id", "test_key2"));
        assertThrows(IllegalArgumentException.class, () -> testClass.updateDictEntity(correctEntityDTO, "test_id2", "test_key"));
        assertDoesNotThrow(() -> testClass.updateDictEntity(correctEntityDTO, "test_id", "test_key"));
    }
}