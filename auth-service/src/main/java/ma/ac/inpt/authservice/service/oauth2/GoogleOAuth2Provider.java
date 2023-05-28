package ma.ac.inpt.authservice.service.oauth2;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.exception.oauth2.EmailNotVerifiedException;
import ma.ac.inpt.authservice.exception.oauth2.GoogleAuthorizationCodeTokenRequestException;
import ma.ac.inpt.authservice.dto.AuthenticationRequest;
import ma.ac.inpt.authservice.dto.RegistrationRequest;
import ma.ac.inpt.authservice.service.auth.RegistrationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;


/**
 * Implementation of the OAuth2Provider interface for Google OAuth2.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuth2Provider implements OAuth2Provider {

    // Dependencies
    private final RegistrationService registrationService;

    // Configuration properties
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    /**
     * Get the name of the OAuth2 provider.
     *
     * @return the provider name
     */
    @Override
    public String getName() {
        return "google";
    }

    /**
     * Authenticate the user using the Google OAuth2 provider.
     *
     * @param authorizationCode the authorization code received from Google
     * @return an AuthenticationRequest object containing user information
     */
    @Override
    public AuthenticationRequest authenticate(String authorizationCode) {
        GoogleTokenResponse tokenResponse;
        GoogleIdToken idToken;

        try {
            tokenResponse = getGoogleAuthorizationCodeTokenRequest(authorizationCode).execute();
            idToken = tokenResponse.parseIdToken();
        } catch (IOException e) {
            log.error("Unable to request or parse Google authorization code", e);
            throw new GoogleAuthorizationCodeTokenRequestException("Unable to request or parse Google authorization code");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        if (!payload.getEmailVerified()) {
            throw new EmailNotVerifiedException("User email not verified by Google");
        }

        // Generate a random password for the OAuth2 user
        String password = UUID.randomUUID().toString();
        String username = payload.getEmail();

        var registrationRequest = RegistrationRequest.builder()
                .username(username)
                .fullName(payload.get("name").toString())
                .email(payload.getEmail())
                .password(password)
                .build();

        // Register the OAuth2 user
        registrationService.registerOauth2User(registrationRequest);

        // Build the authentication request
        return AuthenticationRequest.builder()
                .username(username)
                .withRefreshToken(true)
                .grantType("PASSWORD")
                .password(password)
                .build();
    }

    // Make this method protected for testability
    public GoogleAuthorizationCodeTokenRequest getGoogleAuthorizationCodeTokenRequest(String authorizationCode) {
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        return new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                jsonFactory,
                clientId,
                clientSecret,
                authorizationCode,
                redirectUri);
    }
}