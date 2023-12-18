package pl.lderecki.crudservice.feignClient;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class DictClientAdapter {

    private final DictClient client;

    public final String FIRST_DICT;

    public final String SECOND_DICT;

    public DictClientAdapter(DictClient client, @Value("${dicts.firstDict.key}") String firstDictId,
                             @Value("${dicts.secondDict.key}") String secondDict) {
        this.client = client;
        this.FIRST_DICT = firstDictId;
        this.SECOND_DICT = secondDict;
    }

    public String translate(String dictId, String dictKey) {

        Map<String, String> dictEntity = null;
        try {
            dictEntity = client.translate(dictId, dictKey);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if(Objects.isNull(dictEntity)) {
            return dictKey;
        }

        String result = dictEntity.get("dictValue");

        if (Objects.isNull(result))
            return dictKey;

        return result;
    }
}
