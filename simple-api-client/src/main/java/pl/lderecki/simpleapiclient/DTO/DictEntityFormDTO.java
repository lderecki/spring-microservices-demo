package pl.lderecki.simpleapiclient.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DictEntityFormDTO implements Serializable {

    private String dictId;

    private String dictKey;

    private String dictValue;

    private Boolean disabled;
}
