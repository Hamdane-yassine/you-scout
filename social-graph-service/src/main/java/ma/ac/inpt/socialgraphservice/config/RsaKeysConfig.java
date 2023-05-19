package ma.ac.inpt.socialgraphservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPublicKey;

/**
 * Configuration class for setting up the RSA public key used for authentication.
 */
@ConfigurationProperties(prefix = "rsa")
public record RsaKeysConfig(RSAPublicKey publicKey) {
}
