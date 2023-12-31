package pl.lderecki.simpleapiclient.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import pl.lderecki.simpleapiclient.DTO.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Service
public class ApiClientService {

    private final WebClient webClient;

    private final String apiUrl;

    private final String dictEndpoint;

    private final String crudEndpoint;

    public ApiClientService(WebClient webClient, @Value("${app.api.home.url}") String apiUrl,
                            @Value("${app.api.endpoints.dict}") String dictEndpoint,
                            @Value("${app.api.endpoints.crud}") String crudEndpoint) {
        this.webClient = webClient;
        this.apiUrl = apiUrl;
        this.dictEndpoint = dictEndpoint;
        this.crudEndpoint = crudEndpoint;
    }

    public List<SimpleEntityReadDTO> findAllSimpleEntities(OAuth2AuthorizedClient authorizedClient) throws WebClientException {

        Mono<SimpleEntityReadDTO[]> responseMono = webClient
                .get()
                .uri(apiUrl + crudEndpoint)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(SimpleEntityReadDTO[].class);

        SimpleEntityReadDTO[] response = responseMono.block();

        if (Objects.isNull(response))
            return List.of();

        return List.of(response);
    }

    public SimpleEntityReadDTO findSimpleEntity(OAuth2AuthorizedClient authorizedClient, long id) throws WebClientException {

        Mono<SimpleEntityReadDTO> responseMono = webClient
                .get()
                .uri(apiUrl + crudEndpoint + "/" + id)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(SimpleEntityReadDTO.class);

        SimpleEntityReadDTO response = responseMono.block();

        if (Objects.isNull(response))
            throw new IllegalArgumentException();

        return response;
    }

    public void saveSimpleEntity(OAuth2AuthorizedClient authorizedClient, SimpleEntityWriteDTO toWrite) throws WebClientException {

        Mono<ResponseEntity<SimpleEntityReadDTO>> monoResponse = webClient
                .post()
                .uri(apiUrl + crudEndpoint)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .body(BodyInserters.fromValue(toWrite))
                .retrieve()
                .toEntity(SimpleEntityReadDTO.class);

        ResponseEntity<SimpleEntityReadDTO> responseBody = monoResponse.block();
    }

    public void updateSimpleEntity(OAuth2AuthorizedClient authorizedClient, SimpleEntityWriteDTO updateFrom, long id)  throws WebClientException {

        Mono<ResponseEntity<SimpleEntityReadDTO>> monoResponse = webClient
                .put()
                .uri(apiUrl + crudEndpoint + "/" + id)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .body(BodyInserters.fromValue(updateFrom))
                .retrieve()
                .toEntity(SimpleEntityReadDTO.class);

        ResponseEntity<SimpleEntityReadDTO> responseBody = monoResponse.block();
    }

    public void deleteSimpleEntity(OAuth2AuthorizedClient authorizedClient, long id) throws WebClientException {
        Mono<ResponseEntity<Object>> monoResponse = webClient
                .delete()
                .uri(apiUrl + crudEndpoint + "/" + id)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .toEntity(Object.class);
        ResponseEntity<Object> responseBody = monoResponse.block();
    }

    public List<DictEntityDTO> findDictEntries(OAuth2AuthorizedClient authorizedClient, String dictId) throws WebClientException {

        Mono<DictDTO> responseMono = webClient
                .get()
                .uri(apiUrl + dictEndpoint + "/" + dictId)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(DictDTO.class);

        DictDTO response = responseMono.block();

        if (Objects.isNull(response))
            return List.of();

        return new ArrayList<>(response.getEntities().values());
    }

    public Map<String, DictDTO> findAllDicts(OAuth2AuthorizedClient authorizedClient) throws WebClientException {

        Mono<Map<String, DictDTO>> responseMono = webClient
                .get()
                .uri(apiUrl + dictEndpoint)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, DictDTO>>(){});

        Map<String, DictDTO> responseBody = responseMono.block();

        if (Objects.isNull(responseBody))
            return Map.of();

        return responseBody;
    }

    public void saveDictEntity(OAuth2AuthorizedClient authorizedClient, DictEntityFormDTO toSave) {

        DictEntityDTO toSavePrepared = new DictEntityDTO(toSave);

        Mono<ResponseEntity<DictEntityDTO>> monoResponse = webClient
                .post()
                .uri(apiUrl + dictEndpoint)
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .body(BodyInserters.fromValue(toSavePrepared))
                .retrieve()
                .toEntity(DictEntityDTO.class);

        ResponseEntity<DictEntityDTO> responseBody = monoResponse.block();
    }

    public void updateDictEntity(OAuth2AuthorizedClient authorizedClient, DictEntityFormDTO updateFrom) {

        DictEntityUpdateDTO updateFromPrepared = new DictEntityUpdateDTO(updateFrom);

        Mono<ResponseEntity<DictEntityDTO>> monoResponse = webClient
                .put()
                .uri(apiUrl + dictEndpoint + "/" + updateFrom.getDictId() + "/" + updateFrom.getDictKey())
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .body(BodyInserters.fromValue(updateFromPrepared))
                .retrieve()
                .toEntity(DictEntityDTO.class);

        ResponseEntity<DictEntityDTO> responseBody = monoResponse.block();
    }


}
