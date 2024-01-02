package pl.lderecki.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;

@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain apiHttpSecurity(ServerHttpSecurity http) {
        http
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/simple_entity/**"))
                .authorizeExchange()
                .matchers(new PathPatternParserServerWebExchangeMatcher("/simple_entity/**"))
                .hasAuthority("SCOPE_read_write")
                .and()
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/dict_values/**"))
                .authorizeExchange()
                .matchers(new PathPatternParserServerWebExchangeMatcher("/dict_values/**"))
                .hasAuthority("SCOPE_read_write")
                .and()
                .oauth2ResourceServer()
                .jwt();
        return http.build();
    }
}
