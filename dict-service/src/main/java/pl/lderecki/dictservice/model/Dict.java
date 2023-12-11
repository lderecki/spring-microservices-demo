package pl.lderecki.dictservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "dicts")
@NoArgsConstructor
@AllArgsConstructor
public class Dict {
    @Id
    private String dictId;

    private String dictName;

    @OneToMany(mappedBy = "dict")
    private List<DictEntity> entities = new ArrayList<>();

}
