package pl.lderecki.crudservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lderecki.crudservice.model.SimpleEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface SimpleEntityJpaRepo extends SimpleEntityRepo, JpaRepository<SimpleEntity, Long> {

    @Override
    List<SimpleEntity> findAll();

    @Override
    Optional<SimpleEntity> findById(Long id);

    @Override
    <S extends SimpleEntity> S save(S entity);

    @Override
    boolean existsById(Long id);

    @Override
    void deleteById(Long id);
}
