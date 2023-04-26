package ma.ac.inpt.authservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Configuration class that represents the RSA keys to be used for JWT signing and verification.
 * The public and private keys are loaded from the application properties file using the prefix "rsa".
 */
@ConfigurationProperties(prefix = "rsa")
public record RsaKeysConfig(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
}