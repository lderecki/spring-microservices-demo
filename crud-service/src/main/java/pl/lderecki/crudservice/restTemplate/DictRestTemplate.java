package pl.lderecki.crudservice.restTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class DictRestTemplate {

    private final RestTemplate rest;

    private final EurekaClient eurekaClient;

    @Value("${dicts.service.endpoint}")
    private String endpoint;

    @Value("${dicts.service.name}")
    private String dictServiceName;

    public final String FIRST_DICT;

    public final String SECOND_DICT;

    public DictRestTemplate(RestTemplate rest, EurekaClient eurekaClient, @Value("${dicts.firstDict.key}") String firstDictId,
                            @Value("${dicts.secondDict.key}") String secondDict) {
        this.rest = rest;
        this.eurekaClient = eurekaClient;

        this.FIRST_DICT = firstDictId;
        this.SECOND_DICT = secondDict;
    }

    public String translate(String dictId, String dictKey) {

        InstanceInfo instance = eurekaClient.getNextServerFromEureka(dictServiceName, false);
        if (Objects.isNull(instance))
            return dictKey;

        String baseUrl = instance.getHomePageUrl();
        URI uri = UriComponentsBuilder.fromUriString(baseUrl).path(endpoint).path("/" + dictId).path("/" + dictKey).build().toUri();

        Map<String, String> dictEntity = null;
        try {
            dictEntity = rest.getForObject(uri, HashMap.class);
        }
        catch (RestClientException e) {
            log.error("URI " + uri + " unreachable.");
        }

        if(Objects.isNull(dictEntity)) {
            return dictKey;
        }

        String result = dictEntity.get("dictValue");

        if (Objects.isNull(result))
            return dictKey;

        return result;
    }

/*    private String getInstanceUrl(String serviceName) {
        InstanceInfo instance = eurekaClient.getNextServerFromEureka(dictServiceName, false);

        String baseUrl = instance.getHomePageUrl();
    }*/

}
