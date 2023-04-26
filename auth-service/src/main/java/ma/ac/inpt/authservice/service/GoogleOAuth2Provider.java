package ma.ac.inpt.authservice.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import ma.ac.inpt.authservice.exception.EmailNotVerifiedException;
import ma.ac.inpt.authservice.payload.AuthenticationRequest;
import ma.ac.inpt.authservice.payload.RegistrationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleOAuth2Provider implements OAuth2Provider {

    private final RegistrationService registrationService;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Override
    public String getName() {
        return "google";
    }

    @Override
    public AuthenticationRequest authenticate(String authorizationCode) throws IOException {
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        GoogleTokenResponse tokenResponse;

        tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                new NetHttpTransport(),
                jsonFactory,
                clientId,
                clientSecret,
                authorizationCode,
                redirectUri)
                .execute();

        GoogleIdToken idToken = tokenResponse.parseIdToken();
        GoogleIdToken.Payload payload = idToken.getPayload();
        if (!payload.getEmailVerified()) {
            throw new EmailNotVerifiedException("User email not verified");
        }
        String password = UUID.randomUUID().toString();
        String username = payload.getEmail();
        var registrationRequest = RegistrationRequest.builder().username(username).fullName(payload.get("name").toString()).email(payload.getEmail()).password(password).build();
        registrationService.registerOauth2User(registrationRequest);
        return AuthenticationRequest.builder().username(username).withRefreshToken(true).grantType("PASSWORD").password(password).build();
    }

}
