package pl.lderecki.crudservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleEntityWriteDTO {

    private Long id;
    private String firstDictKey;
    private String secondDictKey;
    private String someTextData;
}
