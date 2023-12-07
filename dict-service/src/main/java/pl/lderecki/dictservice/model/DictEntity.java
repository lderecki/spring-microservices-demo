package pl.lderecki.dictservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

@Data
@Entity
@IdClass(DictCompositeId.class)
@Table(name = "dict_values")
@AllArgsConstructor
@NoArgsConstructor
public class DictEntity {

    @Id
    @Column(name = "dict_id")
    private String dict;
    @Id
    private String dictKey;

    private String dictValue;

    private boolean disabled;
}
