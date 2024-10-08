package ma.ac.inpt.authservice.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for Spring Security.
 * Enables web and method security features and configures AuthenticationManager and SecurityFilterChain.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    /**
     * Configures RSA keys for JWT.
     */
    private final RsaKeysConfig rsakeysConfig;

    /**
     * Configures password encoder.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Configures AuthenticationManager.
     *
     * @param userDetailsService the UserDetailsService implementation
     * @return the configured AuthenticationManager instance
     */
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) {
        // Creates an instance of DaoAuthenticationProvider
        var authProvider = new DaoAuthenticationProvider();
        // Configures the password encoder
        authProvider.setPasswordEncoder(passwordEncoder);
        // Configures the user details service
        authProvider.setUserDetailsService(userDetailsService);
        // Creates an instance of ProviderManager with the DaoAuthenticationProvider
        return new ProviderManager(authProvider);
    }

    /**
     * Configures the security filter chain.
     *
     * @param httpSecurity the HttpSecurity instance
     * @return the configured SecurityFilterChain instance
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Disables CSRF protection
                .csrf(AbstractHttpConfigurer::disable)
                // Permits access to /api/v1/auth/logout for authenticated users
                .authorizeRequests(auth -> auth.antMatchers("/auth/logout").authenticated())
                // Permits access to /api/v1/auth/** for all
                .authorizeRequests(auth -> auth.antMatchers("/auth/**").permitAll())
                // Requires authentication for all other requests
                .authorizeRequests(auth -> auth.anyRequest().authenticated())
                // Configures session management to be stateless
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configures JWT as the OAuth2 resource server
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                // Disables HTTP Basic authentication
                .httpBasic(AbstractHttpConfigurer::disable)
                // Builds the security filter chain
                .build();
    }

    /**
     * Configures JWT decoder with the RSA public key.
     *
     * @return the configured JwtDecoder instance
     */
    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsakeysConfig.publicKey()).build();
    }

    /**
     * Configures JWT encoder with the RSA key pair.
     *
     * @return the configured JwtEncoder instance
     */
    @Bean
    JwtEncoder jwtEncoder() {
        // Builds a JWK from the RSA key pair
        JWK jwk = new RSAKey.Builder(rsakeysConfig.publicKey()).privateKey(rsakeysConfig.privateKey()).build();
        // Configures the JWK source
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
        // Returns an instance of NimbusJwtEncoder with the JWK source
        return new NimbusJwtEncoder(jwkSource);
    }
}
