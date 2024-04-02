package pl.lderecki.aiapiconsumer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class WebClientConfig {

    private final String baseUrl;
    private final String token;

    public WebClientConfig(@Value("${api.baseUrl}") String baseUrl, @Value("${api.token}") String token) {
        this.baseUrl = baseUrl;
        this.token = token;
    }

    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(h -> h.setBearerAuth(token))
                .defaultHeaders(h -> h.setContentType(MediaType.APPLICATION_JSON))
                .defaultHeaders(h -> h.setAccept(List.of(MediaType.APPLICATION_JSON)))
                .codecs(c -> c.defaultCodecs().maxInMemorySize(10000 * 1024))
                .build();
    }
}
