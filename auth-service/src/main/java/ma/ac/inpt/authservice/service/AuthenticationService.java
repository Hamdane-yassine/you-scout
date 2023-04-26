package ma.ac.inpt.authservice.service;

import ma.ac.inpt.authservice.payload.AuthenticationRequest;
import ma.ac.inpt.authservice.payload.AuthenticationResponse;

import java.io.IOException;

public interface AuthenticationService {

    /**
     * Authenticates user credentials provided in the given authentication request and generates an authentication response.
     *
     * @param request - The authentication request containing the user credentials to authenticate.
     * @return An authentication response containing access and refresh tokens.
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse authenticateOAuth2(String provider, String authorizationCode) throws IOException;
}
