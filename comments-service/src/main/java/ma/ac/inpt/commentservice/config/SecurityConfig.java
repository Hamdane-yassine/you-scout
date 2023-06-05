package ma.ac.inpt.commentservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for security.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final RsaKeysConfig rsakeysConfig;

    /**
     * Creates the security filter chain.
     *
     * @param httpSecurity the HttpSecurity object
     * @return the security filter chain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Disables CSRF protection
                .csrf(AbstractHttpConfigurer::disable)
                // Requires authentication for all other requests
                .authorizeRequests(auth -> auth.anyRequest().authenticated())
                // Configures JWT as the OAuth2 resource server
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                // Builds the security filter chain
                .build();
    }

    /**
     * Configures the JWT decoder with the RSA public key.
     *
     * @return the JWT decoder
     */
    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsakeysConfig.publicKey()).build();
    }

}
