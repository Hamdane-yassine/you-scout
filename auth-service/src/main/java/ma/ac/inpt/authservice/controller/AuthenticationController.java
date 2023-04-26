package ma.ac.inpt.authservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.payload.AuthenticationRequest;
import ma.ac.inpt.authservice.payload.AuthenticationResponse;
import ma.ac.inpt.authservice.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

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
     *
     * @param request the authentication request data
     * @return the authentication response containing a JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        log.info("Received authentication request for username {}", request.getUsername());
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/oauth2/callback")
    public ResponseEntity<?> handleGoogleCallback(@RequestParam(value = "code") String authorizationCode) {
        AuthenticationResponse authenticationResponse;
        try {
            authenticationResponse = authenticationService.authenticateOAuth2("google", authorizationCode);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }

        return ResponseEntity.ok(authenticationResponse);
    }
}



