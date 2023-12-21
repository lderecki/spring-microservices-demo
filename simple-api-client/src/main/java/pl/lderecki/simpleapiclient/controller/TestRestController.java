package pl.lderecki.simpleapiclient.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import pl.lderecki.simpleapiclient.DTO.SimpleEntityReadDTO;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class TestRestController {

    private WebClient webClient;

    public TestRestController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping(value = "/test")
    public SimpleEntityReadDTO[] getArticles(
            @RegisteredOAuth2AuthorizedClient("api-client-authorization-code") OAuth2AuthorizedClient authorizedClient
    ) {

        return this.webClient
                .get()
                .uri("http://127.0.0.1:8080/simple_entity")
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(SimpleEntityReadDTO[].class)
                .block();
    }

}
