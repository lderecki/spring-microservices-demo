package pl.lderecki.simpleapiclient.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictDTO implements Serializable {

    private String dictId;

    private String dictName;

    private Map<String, DictEntityDTO> entities = new HashMap<>();

}
