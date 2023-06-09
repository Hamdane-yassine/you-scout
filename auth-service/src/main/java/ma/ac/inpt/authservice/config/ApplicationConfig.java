package ma.ac.inpt.authservice.config;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.authservice.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for setting up the application.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    /**
     * The user repository.
     */
    private final UserRepository userRepository;

    /**
     * Bean definition for the UserDetailsService.
     * Retrieves user details from the UserRepository based on the provided username or email.
     *
     * @return the UserDetailsService implementation
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return usernameOrEmail -> userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Bean definition for the PasswordEncoder.
     * Returns an instance of BCryptPasswordEncoder for password hashing.
     *
     * @return the PasswordEncoder implementation
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


