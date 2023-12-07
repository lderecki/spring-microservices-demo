package pl.lderecki.dictservice.model;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "dicts")
public class Dict {
    @Id
    private String dictId;

    private String dictName;

    @OneToMany(mappedBy = "dict")
    private List<DictEntity> entities = new ArrayList<>();

}
