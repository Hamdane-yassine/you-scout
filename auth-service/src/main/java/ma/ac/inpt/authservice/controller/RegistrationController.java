package ma.ac.inpt.authservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.payload.RegistrationRequest;
import ma.ac.inpt.authservice.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Controller class for handling registration requests.
 */
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    /**
     * Endpoint for registering a new user.
     * @param request the registration request data
     * @return the authentication response containing a JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest request) {
        log.info("Received registration request for username {}", request.getUsername());
        String message = registrationService.register(request);
        return ResponseEntity.ok(message);
    }



}
