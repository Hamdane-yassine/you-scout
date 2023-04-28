package ma.ac.inpt.authservice;

import ma.ac.inpt.authservice.config.RsaKeysConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * AuthServiceApplication is the main entry point for the authentication service.
 * It includes the following configurations:
 * - RsaKeysConfig: enables configuration properties for the RSA keys
 * - EnableAsync: enables asynchronous processing
 */
@SpringBootApplication
@EnableConfigurationProperties(RsaKeysConfig.class)
@EnableAsync
public class AuthServiceApplication {

    /**
     * The main method that starts the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }

    /**
     * Creates a custom ThreadPoolTaskExecutor that handles asynchronous tasks.
     * It defines the core pool size, max pool size, queue capacity, and thread name prefix.
     *
     * @return the configured ThreadPoolTaskExecutor instance
     */
    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("EmailThread-");
        executor.initialize();
        return executor;
    }

}

