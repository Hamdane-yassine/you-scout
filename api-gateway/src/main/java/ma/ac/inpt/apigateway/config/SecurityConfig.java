package ma.ac.inpt.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * This class is the Security configuration for the API gateway.
 * It enables Spring WebFlux security and provides a SecurityWebFilterChain
 * and a ReactiveJwtDecoder bean. The SecurityWebFilterChain defines the
 * authorization rules and the OAuth2ResourceServer configuration, while
 * the ReactiveJwtDecoder decodes the JWT tokens using the RSA public key.
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final RsaKeysConfig rsakeysConfig;

    /**
     * Defines the authorization rules for the API endpoints and sets up the
     * JWT token validation configuration.
     *
     * @param http The ServerHttpSecurity instance to configure.
     * @return A SecurityWebFilterChain instance.
     */
    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.csrf().disable() // Disable CSRF protection
                .authorizeExchange().pathMatchers("/api/v1/auth/register", "/api/v1/auth/authenticate").permitAll().anyExchange().authenticated().and().oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt).build();
    }

    /**
     * Creates a ReactiveJwtDecoder bean to decode JWT tokens using the RSA public key.
     *
     * @return A ReactiveJwtDecoder instance.
     */
    @Bean
    ReactiveJwtDecoder jwtDecoder() {
        return NimbusReactiveJwtDecoder.withPublicKey(rsakeysConfig.publicKey()).build();
    }

}
