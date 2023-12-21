package pl.lderecki.simpleapiclient.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleEntityReadDTO {

    private long id;
    private String firstDictionaryValue;
    private String secondDictionaryValue;
    private String someTextData;
}
