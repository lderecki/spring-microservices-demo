package pl.lderecki.dictservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.lderecki.dictservice.model.DictCompositeId;
import pl.lderecki.dictservice.model.DictEntity;

import java.util.List;

@Repository
public interface DictEntityJpaRepo extends DictEntityRepo, JpaRepository<DictEntity, DictCompositeId> {

    @Override
    List<DictEntity> findAll();

    @Override
    <S extends DictEntity> S save(S entity);
}
