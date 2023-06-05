package ma.ac.inpt.postservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;

@Primary
@ConfigurationProperties(prefix = "test")
public record RsaKeysConfig(String publicKey) {
}
