package pl.lderecki.dictservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.lderecki.dictservice.model.Dict;

import java.util.List;

@Repository
public interface DictJpaRepo extends DictRepo, JpaRepository<Dict, String> {

    @Override
    @Query("from Dict as d left join fetch d.entities as e")
    List<Dict> findAll();

    @Override
    Dict findByDictId(String id);
}
