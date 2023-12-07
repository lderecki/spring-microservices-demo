package pl.lderecki.dictservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lderecki.dictservice.model.DictEntity;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictEntityDTO implements Serializable {

    private String dictId;

    private String dictKey;

    private String dictValue;

    public DictEntityDTO(DictEntity dictEntity) {
        this.dictId = dictEntity.getDict();
        this.dictKey = dictEntity.getDictKey();
        this.dictValue = dictEntity.getDictValue();
    }
}
