package ma.ac.inpt.apigateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPublicKey;

/**

 This configuration class represents the RSA public key used for JWT token validation.
 It is used to map the properties with the prefix "rsa" in the application.properties file.
 The RSAPublicKey object is passed as a parameter to the RsaKeysConfig record constructor.
 */

@ConfigurationProperties(prefix = "rsa")
public record RsaKeysConfig(RSAPublicKey publicKey) {
}
