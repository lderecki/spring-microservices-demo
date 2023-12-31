package pl.lderecki.dictservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lderecki.dictservice.model.Dict;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictRepoDTO implements Serializable {

    private String dictId;

    private String dictName;

    private Map<String, DictEntityRepoDTO> entities = new HashMap<>();

    public DictRepoDTO(Dict dict) {
        this.dictId = dict.getDictId();
        this.dictName = dict.getDictName();
        dict.getEntities().forEach(e -> entities.put(e.getDictKey(), new DictEntityRepoDTO(e)));
    }
}
