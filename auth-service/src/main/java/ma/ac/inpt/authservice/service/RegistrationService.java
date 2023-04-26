package ma.ac.inpt.authservice.service;

import ma.ac.inpt.authservice.payload.RegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public interface RegistrationService {

    /**
     * Registers a new user with the given details and returns an authentication response
     * containing an access token and an optional refresh token.
     *
     * @param request the registration request containing user details
     * @return an authentication response containing an access token and an optional refresh token
     */
    String register(RegistrationRequest request);

    void registerOauth2User(RegistrationRequest request);
}
