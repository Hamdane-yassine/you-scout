package ma.ac.inpt.apigateway;

import ma.ac.inpt.apigateway.config.RsaKeysConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeysConfig.class)
@SpringBootApplication(exclude = PropertyPlaceholderAutoConfiguration.class)
public class ApiGateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGateWayApplication.class);
    }
}
