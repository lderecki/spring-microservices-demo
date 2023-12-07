package pl.lderecki.dictservice.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class DictCompositeId implements Serializable {

    private String dict;
    private String dictKey;
}
