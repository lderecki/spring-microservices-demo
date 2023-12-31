package pl.lderecki.dictservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lderecki.dictservice.model.DictEntity;

import javax.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictEntityRepoDTO implements Serializable {

    private String dictId;

    private String dictKey;

    private String dictValue;

    private boolean disabled;

    public DictEntityRepoDTO(DictEntity dictEntity) {
        this.dictId = dictEntity.getDict();
        this.dictKey = dictEntity.getDictKey();
        this.dictValue = dictEntity.getDictValue();
        this.disabled = dictEntity.isDisabled();
    }
}
