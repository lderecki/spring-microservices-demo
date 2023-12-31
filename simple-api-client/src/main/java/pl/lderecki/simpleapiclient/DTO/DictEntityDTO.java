package pl.lderecki.simpleapiclient.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictEntityDTO implements Serializable {

    private String dictId;

    private String dictKey;

    private String dictValue;

    public DictEntityDTO(DictEntityFormDTO from) {
        this.dictId = from.getDictId();
        this.dictKey = from.getDictKey();
        this.dictValue = from.getDictValue();
    }
}
