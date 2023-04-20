package ma.ac.inpt.authservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.payload.AuthenticationRequest;
import ma.ac.inpt.authservice.payload.AuthenticationResponse;
import ma.ac.inpt.authservice.payload.RegisterRequest;
import ma.ac.inpt.authservice.service.AuthenticationService;
import ma.ac.inpt.authservice.service.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for handling authentication requests.
 */
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Endpoint for authenticating a user and generating a JWT token.
     * @param request the authentication request data
     * @return the authentication response containing a JWT token
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        log.info("Received authentication request for username {}", request.getUsername());
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }
}


