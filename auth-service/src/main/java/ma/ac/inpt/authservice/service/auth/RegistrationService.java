package ma.ac.inpt.authservice.service.auth;

import ma.ac.inpt.authservice.dto.RegistrationRequest;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user registration.
 */
@Service
public interface RegistrationService {

    /**
     * Register a user with the given registration request.
     * @param request The registration request containing the user's details.
     * @return A message indicating the success of the registration process.
     */
    String register(RegistrationRequest request);

    /**
     * Register a user who authenticated via OAuth2 with the given registration request.
     * @param request The registration request containing the user's details.
     */
    void registerOauth2User(RegistrationRequest request);
}

