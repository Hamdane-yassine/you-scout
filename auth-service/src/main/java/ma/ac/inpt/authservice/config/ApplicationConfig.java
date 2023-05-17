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
     * Creates a new instance of the UserDetailsService using the user repository.
     *
     * @return the UserDetailsService instance
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsernameOrEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Creates a new instance of the PasswordEncoder.
     *
     * @return the PasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

