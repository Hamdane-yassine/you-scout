package ma.ac.inpt.authservice.service.oauth2;

import ma.ac.inpt.authservice.payload.AuthenticationRequest;

/**
 * Interface for an OAuth2 provider that can authenticate users.
 */
public interface OAuth2Provider {

    /**
     * Gets the name of the OAuth2 provider.
     *
     * @return the name of the provider
     */
    String getName();

    /**
     * Authenticates the user with the given authorization code.
     *
     * @param authorizationCode the authorization code to use for authentication
     * @return an authentication request containing the user's information
     */
    AuthenticationRequest authenticate(String authorizationCode);
}

