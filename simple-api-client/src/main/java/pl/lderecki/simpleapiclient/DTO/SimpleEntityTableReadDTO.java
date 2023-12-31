package pl.lderecki.simpleapiclient.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleEntityTableReadDTO {

    private long id;
    private String firstDictionaryKey;
    private String firstDictionaryValue;
    private String secondDictionaryKey;
    private String secondDictionaryValue;
    private String someTextData;
}
