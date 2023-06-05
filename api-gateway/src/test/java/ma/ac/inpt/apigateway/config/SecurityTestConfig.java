package ma.ac.inpt.apigateway.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Test configuration class for security settings.
 */
@TestConfiguration
@EnableWebFluxSecurity
public class SecurityTestConfig {

    /**
     * Configures the security web filter chain for testing purposes.
     *
     * @param http The ServerHttpSecurity instance to configure.
     * @return A SecurityWebFilterChain instance.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .anyExchange().permitAll() // Allow access to any endpoint without authentication for testing purposes
                .and().csrf().disable() // Disable CSRF protection for testing purposes
                .build();
    }
}

