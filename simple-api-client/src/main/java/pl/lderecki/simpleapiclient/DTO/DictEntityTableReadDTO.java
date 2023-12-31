package pl.lderecki.simpleapiclient.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictEntityTableReadDTO implements Serializable {

    private String dictId;
    private String dictName;

    private String dictKey;

    private String dictValue;
}
