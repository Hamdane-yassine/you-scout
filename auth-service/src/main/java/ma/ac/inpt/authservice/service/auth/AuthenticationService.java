package ma.ac.inpt.authservice.service.auth;

import ma.ac.inpt.authservice.payload.AuthenticationRequest;
import ma.ac.inpt.authservice.payload.AuthenticationResponse;

/**
 * Service interface for authenticating users.
 */
public interface AuthenticationService {

    /**
     * Authenticates the user with the given credentials.
     *
     * @param request the authentication request containing the user credentials
     * @return the authentication response indicating whether the authentication was successful and, if so, the JWT token to be used for further requests
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);

    /**
     * Authenticates the user using OAuth2 protocol with the given provider and authorization code.
     *
     * @param provider          the OAuth2 provider (e.g. Google, Facebook, etc.)
     * @param authorizationCode the authorization code obtained from the OAuth2 provider
     * @return the authentication response indicating whether the authentication was successful and, if so, the JWT token to be used for further requests
     */
    AuthenticationResponse authenticateOAuth2(String provider, String authorizationCode);
}

