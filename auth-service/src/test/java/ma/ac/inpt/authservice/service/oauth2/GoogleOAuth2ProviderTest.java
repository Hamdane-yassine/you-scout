package ma.ac.inpt.authservice.service.oauth2;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import ma.ac.inpt.authservice.dto.AuthenticationRequest;
import ma.ac.inpt.authservice.dto.RegistrationRequest;
import ma.ac.inpt.authservice.service.auth.RegistrationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleOAuth2ProviderTest {

    @InjectMocks
    private GoogleOAuth2Provider googleOAuth2Provider;

    @Mock
    private RegistrationService registrationService;

    @Test
    @DisplayName("Test GetName")
    void testGetName() {
        // When the getName() method is called
        String name = googleOAuth2Provider.getName();

        // Then the provider name should be "google"
        assertEquals("google", name);
    }

    @Test
    @DisplayName("Test Authenticate")
    void testAuthenticate() throws Exception {
        // Given a valid authorization code
        String authCode = "auth-code";

        // And a parsed token response
        GoogleTokenResponse mockTokenResponse = Mockito.mock(GoogleTokenResponse.class);
        GoogleIdToken mockIdToken = Mockito.mock(GoogleIdToken.class);

        // Create a real instance of Payload and set required fields
        GoogleIdToken.Payload payload = new GoogleIdToken.Payload();
        payload.setEmailVerified(true);
        payload.setEmail("user@gmail.com");
        payload.set("name", "John Doe");

        GoogleOAuth2Provider testGoogleProvider = Mockito.spy(googleOAuth2Provider);

        // Mock getGoogleAuthorizationCodeTokenRequest
        GoogleAuthorizationCodeTokenRequest mockTokenRequest = Mockito.mock(GoogleAuthorizationCodeTokenRequest.class);
        doReturn(mockTokenRequest).when(testGoogleProvider).getGoogleAuthorizationCodeTokenRequest(any());

        when(mockTokenRequest.execute()).thenReturn(mockTokenResponse);
        when(mockTokenResponse.parseIdToken()).thenReturn(mockIdToken);
        when(mockIdToken.getPayload()).thenReturn(payload);

        // Capture the registration request argument
        ArgumentCaptor<RegistrationRequest> registrationRequestCaptor = ArgumentCaptor.forClass(RegistrationRequest.class);

        // When authenticate() is called
        AuthenticationRequest authenticationRequest = testGoogleProvider.authenticate(authCode);

        // Then a user should be registered
        verify(registrationService, times(1)).registerOauth2User(registrationRequestCaptor.capture());

        RegistrationRequest capturedRegistrationRequest = registrationRequestCaptor.getValue();

        // And an authentication request should be returned with the user's username and password
        assertEquals(payload.getEmail(), capturedRegistrationRequest.getUsername());
        assertEquals(payload.get("name"), capturedRegistrationRequest.getFullName());
        assertEquals(payload.getEmail(), capturedRegistrationRequest.getEmail());
        assertEquals(payload.getEmail(), authenticationRequest.getUsername());
        assertEquals(capturedRegistrationRequest.getPassword(), authenticationRequest.getPassword());
    }


}

