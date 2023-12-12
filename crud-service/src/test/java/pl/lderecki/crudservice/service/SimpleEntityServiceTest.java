package pl.lderecki.crudservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.lderecki.crudservice.DTO.SimpleEntityReadDTO;
import pl.lderecki.crudservice.DTO.SimpleEntityWriteDTO;
import pl.lderecki.crudservice.model.SimpleEntity;
import pl.lderecki.crudservice.repo.SimpleEntityRepo;
import pl.lderecki.crudservice.restTemplate.DictRestTemplate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SimpleEntityServiceTest {

    @Mock
    private SimpleEntityRepo repo;

    @Mock
    private DictRestTemplate restTemplate;

    @InjectMocks
    private SimpleEntityService testClass;

    @BeforeEach
    private void setFinals() throws Exception{
        Field FIRST_DICTfield = ReflectionUtils
                .findFields(DictRestTemplate.class, f -> f.getName().equals("FIRST_DICT"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);
        Field SECOND_DICTfield = ReflectionUtils
                .findFields(DictRestTemplate.class, f -> f.getName().equals("SECOND_DICT"),
                        ReflectionUtils.HierarchyTraversalMode.TOP_DOWN)
                .get(0);
        FIRST_DICTfield.setAccessible(true);
        SECOND_DICTfield.setAccessible(true);
        FIRST_DICTfield.set(restTemplate, "test_id");
        SECOND_DICTfield.set(restTemplate, "test_id2");
    }
    @Test
    void findAll() throws Exception {

        List<SimpleEntity> entities = new ArrayList<>();
        SimpleEntity simpleEntity = new SimpleEntity(1L, "test_key1", "test_key2", "test_data");
        entities.add(simpleEntity);

        List<SimpleEntityReadDTO> dtos = entities.stream()
                        .map(e -> new SimpleEntityReadDTO(simpleEntity.getId(), "test_value1",
                                "test_value2", "test_data"))
                        .collect(Collectors.toList());

        when(repo.findAll()).thenReturn(entities);
        when(restTemplate.translate("test_id", "test_key1")).thenReturn("test_value1");
        when(restTemplate.translate("test_id2", "test_key2")).thenReturn("test_value2");

        assertEquals(dtos, testClass.findAll());
    }

    @Test
    void findById() throws Exception{

        SimpleEntity simpleEntity = new SimpleEntity(1L, "test_key1", "test_key2", "test_data");
        SimpleEntityReadDTO dto = new SimpleEntityReadDTO(simpleEntity.getId(), "test_value1",
                                                          "test_value2", simpleEntity.getSomeTextData());

        when(repo.findById(1L)).thenReturn(Optional.of(simpleEntity));
        when(repo.findById(2L)).thenReturn(Optional.empty());
        when(restTemplate.translate("test_id", "test_key1")).thenReturn("test_value1");
        when(restTemplate.translate("test_id2", "test_key2")).thenReturn("test_value2");

        assertEquals(dto, testClass.findById(dto.getId()));
        assertThrows(IllegalArgumentException.class, () -> testClass.findById(2));
    }

    @Test
    void save() throws Exception{
        
        SimpleEntity simpleEntity = new SimpleEntity(null, "test_key1", "test_key2", "test_data");
        SimpleEntityWriteDTO simpleEntityWriteDTO = new SimpleEntityWriteDTO(5L, simpleEntity.getFirstDictKey(),
                                                    simpleEntity.getSecondDictKey(), simpleEntity.getSomeTextData());
        SimpleEntity returnEntity = new SimpleEntity(1L, "test_key1", "test_key2", "test_data");
        SimpleEntityReadDTO dto = new SimpleEntityReadDTO(1L, "test_value1",
                                        "test_value2", simpleEntity.getSomeTextData());

        when(restTemplate.translate("test_id", "test_key1")).thenReturn("test_value1");
        when(restTemplate.translate("test_id2", "test_key2")).thenReturn("test_value2");
        when(repo.save(simpleEntity)).thenReturn(returnEntity);

        assertEquals(dto, testClass.save(simpleEntityWriteDTO));
    }

    @Test
    void update() {

        SimpleEntity simpleEntity = new SimpleEntity(1L, "test_key1", "test_key2", "another_test_data");
        SimpleEntityWriteDTO simpleEntityWriteDTO = new SimpleEntityWriteDTO(1L, simpleEntity.getFirstDictKey(),
                                        simpleEntity.getSecondDictKey(), "another_test_data");
        SimpleEntityWriteDTO badSimpleEntityWriteDTO = new SimpleEntityWriteDTO(2L, simpleEntity.getFirstDictKey(),
                                        simpleEntity.getSecondDictKey(), "another_test_data");
        SimpleEntity returnEntity = new SimpleEntity(1L, "test_key1", "test_key2", "another_test_data");


        when(repo.existsById(1L)).thenReturn(true);
        when(repo.existsById(2L)).thenReturn(false);
        when(repo.save(simpleEntity)).thenReturn(returnEntity);

        assertDoesNotThrow(() -> testClass.update(simpleEntityWriteDTO));
        assertThrows(IllegalArgumentException.class, () -> testClass.update(badSimpleEntityWriteDTO));
    }

    @Test
    void delete() {
        when(repo.existsById(1L)).thenReturn(true);
        when(repo.existsById(2L)).thenReturn(false);

        assertDoesNotThrow(() -> testClass.delete(1L));
        assertThrows(IllegalArgumentException.class, () -> testClass.delete(2L));
    }
}