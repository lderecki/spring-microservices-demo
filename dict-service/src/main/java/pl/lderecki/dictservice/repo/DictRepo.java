package pl.lderecki.dictservice.repo;

import pl.lderecki.dictservice.model.Dict;

import java.util.List;


public interface DictRepo {
    List<Dict> findAll();

    Dict findByDictId(String id);
}
