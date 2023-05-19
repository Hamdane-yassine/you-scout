package ma.ac.inpt.socialgraphservice;

import ma.ac.inpt.socialgraphservice.config.RsaKeysConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeysConfig.class)
public class SocialGraphApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocialGraphApplication.class, args);
    }}