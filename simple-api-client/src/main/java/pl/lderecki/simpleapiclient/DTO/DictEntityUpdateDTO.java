package pl.lderecki.simpleapiclient.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictEntityUpdateDTO implements Serializable {

    private String dictValue;
    private boolean disabled;

    public DictEntityUpdateDTO(DictEntityFormDTO from) {
        this.dictValue = from.getDictValue();
        this.disabled = from.getDisabled();
    }
}
