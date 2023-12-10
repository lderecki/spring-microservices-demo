package pl.lderecki.crudservice.repo;

import pl.lderecki.crudservice.model.SimpleEntity;

import java.util.List;
import java.util.Optional;

public interface SimpleEntityRepo {

    List<SimpleEntity> findAll();

    Optional<SimpleEntity> findById(Long id);

    <S extends SimpleEntity> S save(S entity);

    boolean existsById(Long id);

    void deleteById(Long id);
}
