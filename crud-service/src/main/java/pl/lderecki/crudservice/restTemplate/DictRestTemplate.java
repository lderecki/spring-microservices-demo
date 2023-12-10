package pl.lderecki.crudservice.restTemplate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class DictRestTemplate {

    private final RestTemplate rest;

    @Value("${dicts.service.url}")
    private String baseUrl;

    public final String FIRST_DICT;

    public final String SECOND_DICT;

    public DictRestTemplate(RestTemplate rest, @Value("${dicts.firstDict.key}") String firstDictId,
                            @Value("${dicts.secondDict.key}") String secondDict) {
        this.rest = rest;

        this.FIRST_DICT = firstDictId;
        this.SECOND_DICT = secondDict;
    }

    public String translate(String dictId, String dictKey) {
        URI uri = UriComponentsBuilder.fromUriString(baseUrl).path("/" + dictId).path("/" + dictKey).build().toUri();

        Map<String, String> dictEntity = null;
        try {
            dictEntity = rest.getForObject(uri, HashMap.class);
        }
        catch (RestClientException e)
        { }

        if(Objects.isNull(dictEntity)) {
            return dictKey;
        }

        String result = dictEntity.get("dictValue");

        if (Objects.isNull(result))
            return dictKey;

        return result;
    }

}
