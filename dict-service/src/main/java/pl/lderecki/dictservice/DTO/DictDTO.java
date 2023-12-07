package pl.lderecki.dictservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lderecki.dictservice.model.Dict;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictDTO implements Serializable {

    private String dictId;

    private String dictName;

    private Map<String, DictEntityDTO> entities = new HashMap<>();

    public DictDTO(Dict dict) {
        this.dictId = dict.getDictId();
        this.dictName = dict.getDictName();
        dict.getEntities().forEach(e -> entities.put(e.getDictKey(), new DictEntityDTO(e)));
    }
}
