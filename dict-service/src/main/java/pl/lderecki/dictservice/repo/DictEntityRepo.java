package pl.lderecki.dictservice.repo;

import pl.lderecki.dictservice.model.DictEntity;

import java.util.List;

public interface DictEntityRepo {

    List<DictEntity> findAll();
    <S extends DictEntity> S save(S entity);
}
