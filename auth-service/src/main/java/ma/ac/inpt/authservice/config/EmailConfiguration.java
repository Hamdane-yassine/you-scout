package ma.ac.inpt.authservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Configuration class for setting up the JavaMailSender used for sending emails.
 */
@Configuration
public class EmailConfiguration {

    /**
     * The email host.
     */
    @Value("${spring.mail.host}")
    private String host;

    /**
     * The email port.
     */
    @Value("${spring.mail.port}")
    private Integer port;

    /**
     * The email username.
     */
    @Value("${spring.mail.username}")
    private String username;

    /**
     * The email password.
     */
    @Value("${spring.mail.password}")
    private String password;

    /**
     * Creates a new instance of the JavaMailSender using the configured properties.
     *
     * @return the JavaMailSender instance
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSenderImpl = new JavaMailSenderImpl();
        mailSenderImpl.setHost(host);
        mailSenderImpl.setPort(port);
        mailSenderImpl.setUsername(username);
        mailSenderImpl.setPassword(password);
        return mailSenderImpl;
    }
}

