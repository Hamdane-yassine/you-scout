package ma.ac.inpt.authservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.dto.*;
import ma.ac.inpt.authservice.service.auth.EmailVerificationService;
import ma.ac.inpt.authservice.service.auth.AuthenticationService;
import ma.ac.inpt.authservice.service.auth.PasswordResetService;
import ma.ac.inpt.authservice.service.auth.RegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Controller class for managing user authentication and authorization.
 * Provides endpoints for user registration, authentication, account verification, and password reset.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    // Services for registration, authentication, account verification, and password reset
    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;
    private final EmailVerificationService emailVerificationService;
    private final PasswordResetService passwordResetService;

    /**
     * Endpoint for user registration.
     * Validates registration request using the RegistrationRequest DTO.
     * Returns HTTP 200 OK status with a success message on successful registration.
     *
     * @param request The registration request.
     * @return A response entity with a success message.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegistrationRequest request) {
        log.info("Received registration request for username {}", request.getUsername());
        String message = registrationService.register(request);
        log.info("User {} registered successfully", request.getUsername());
        return ResponseEntity.ok(message);
    }

    /**
     * Endpoint for user authentication.
     * Validates authentication request using the AuthenticationRequest DTO.
     * Returns HTTP 200 OK status with an authentication response containing JWT on successful authentication.
     *
     * @param request The authentication request.
     * @return A response entity with the authentication response.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        log.info("Received authentication request for username {}", request.getUsername());
        AuthenticationResponse response = authenticationService.authenticate(request);
        log.info("User {} authenticated successfully", request.getUsername());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for account verification.
     * Expects a token parameter in the request query string.
     * Returns HTTP 200 OK status with a success message on successful account verification.
     *
     * @param token The verification token.
     * @return A response entity with a success message.
     */
    @GetMapping("/confirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {
        String message = emailVerificationService.verifyAccount(token);
        log.info("Account verification successful for token {}", token);
        return ResponseEntity.ok(message);
    }

    /**
     * Endpoint for requesting a password reset email.
     * Validates password reset request using the ForgotPasswordRequest DTO.
     * Returns HTTP 200 OK status with a success message on successful password reset request.
     *
     * @param request The password reset request.
     * @return A response entity with a success message.
     */
    @PostMapping("/password/forgot")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String message = passwordResetService.sendPasswordResetEmail(request);
        log.info("Password reset email sent to user with email {}", request.getEmail());
        return ResponseEntity.ok(message);
    }

    /**
     * Endpoint for resetting password.
     * Validates reset password request using the ResetPasswordRequest DTO.
     * Expects a token parameter in the request query string.
     * Returns HTTP 200 OK status with a success message on successful password reset.
     *
     * @param request The reset password request.
     * @param token   The password reset token.
     * @return A response entity with a success message.
     */
    @PostMapping("/password/reset")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request, @RequestParam String token) {
        String message = passwordResetService.resetPassword(request, token);
        log.info("Password reset successful for user with email {}", request.getEmail());
        return ResponseEntity.ok(message);
    }

    /**
     * Endpoint for handling Google OAuth2 callback.
     * Expects an authorization code parameter in the request query string.
     * Returns HTTP 200 OK status with an authentication response containing JWT on successful authentication.
     *
     * @param authorizationCode The authorization code.
     * @return A response entity with the authentication response.
     */
    @GetMapping("/oauth2/callback")
    public ResponseEntity<AuthenticationResponse> handleGoogleCallback(@RequestParam(value = "code") String authorizationCode) {
        log.info("Received OAuth2 Google authentication request");
        AuthenticationResponse response = authenticationService.authenticateOAuth2("google", authorizationCode);
        log.info("OAuth2 Google authentication successful");
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for user logout.
     * Invalidates the refresh token for the user, effectively logging them out.
     * Returns HTTP 200 OK status with a success message on successful logout.
     *
     * @param principal The current user.
     * @return A response entity with a success message.
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(Principal principal) {
        String username = principal.getName();
        log.info("Received logout request for username {}", username);
        authenticationService.logout(username);
        log.info("User {} logged out successfully", username);
        return ResponseEntity.ok("User logged out successfully.");
    }
}
