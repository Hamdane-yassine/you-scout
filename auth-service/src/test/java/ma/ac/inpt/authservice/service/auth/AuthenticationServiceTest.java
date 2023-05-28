package ma.ac.inpt.authservice.service.auth;

import ma.ac.inpt.authservice.dto.AuthenticationRequest;
import ma.ac.inpt.authservice.dto.AuthenticationResponse;
import ma.ac.inpt.authservice.model.Role;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.repository.RefreshTokenRepository;
import ma.ac.inpt.authservice.repository.UserRepository;
import ma.ac.inpt.authservice.service.oauth2.OAuth2Provider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    OAuth2Provider oAuth2Provider;
    List<OAuth2Provider> oAuth2Providers;
    @Mock
    private JwtEncoder jwtEncoder;
    @Mock
    private JwtDecoder jwtDecoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailVerificationService emailVerificationService;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    private AuthenticationServiceImpl authenticationService;

    private Jwt jwt;
    private User user;

    @BeforeEach
    public void setUp() {
        oAuth2Providers = Collections.singletonList(oAuth2Provider);

        authenticationService = new AuthenticationServiceImpl(jwtEncoder, jwtDecoder, authenticationManager, userRepository, emailVerificationService, oAuth2Providers, refreshTokenRepository);

        // Create a Jwt mock object
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");

        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", "testUser");
        claims.put("exp", System.currentTimeMillis() / 1000L + 3600); // add one hour
        claims.put("iat", System.currentTimeMillis() / 1000L);

        jwt = new Jwt("testToken", Instant.now(), Instant.now().plusSeconds(60), headers, claims);

        // Create a mock Role and add it to a Set
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().roleName("SCOPE_USER").build());

        // Create a User with non-null roles
        user = User.builder()
                .username("testUser")
                .password("password")
                .isEnabled(true)
                .roles(roles) // Pass the non-null roles Set
                .build();
    }

    @DisplayName("Test authenticate with password grant")
    @Test
    public void authenticate_withPasswordGrant_successful() {
        //Given
        AuthenticationRequest request = new AuthenticationRequest();
        request.setGrantType("password");
        request.setUsername("username");
        request.setPassword("password");

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(auth.getName()).thenReturn("username");
        Collection<? extends GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("SCOPE_USER"));
        when(auth.getAuthorities()).thenAnswer((Answer<Collection<? extends GrantedAuthority>>) invocation -> authorities);

        when(jwtEncoder.encode(any())).thenReturn(jwt);

        //When
        AuthenticationResponse response = authenticationService.authenticate(request);

        //Then
        assertNotNull(response);
        assertEquals("testToken", response.getAccessToken());
    }

    @DisplayName("Test authenticate with refresh token grant")
    @Test
    public void authenticate_withRefreshTokenGrant_successful() {
        // Given
        AuthenticationRequest request = new AuthenticationRequest();
        request.setGrantType("refresh_token");
        request.setRefreshToken("refreshToken");

        when(jwtDecoder.decode(any())).thenReturn(jwt);
        when(jwtEncoder.encode(any())).thenReturn(jwt);
        when(refreshTokenRepository.existsByTokenUuid(any())).thenReturn(true);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        // When
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Then
        assertNotNull(response);
    }

    @DisplayName("Test authenticate with unsupported grant type")
    @Test
    public void authenticate_withUnsupportedGrantType_throwsException() {
        //Given
        AuthenticationRequest request = new AuthenticationRequest();
        request.setGrantType("unsupported");

        //When
        Exception exception = assertThrows(IllegalArgumentException.class, () -> authenticationService.authenticate(request));

        //Then
        assertEquals("Unsupported grant type : UNSUPPORTED", exception.getMessage());
    }

    @DisplayName("Test authenticateOAuth2 with valid provider and authorization code")
    @Test
    public void authenticateOAuth2_withValidProviderAndAuthorizationCode_successful() {
        //Given
        String provider = "google";
        String authorizationCode = "authorizationCode";

        when(oAuth2Provider.getName()).thenReturn(provider);

        // Create the service here, after the mocks have been configured
        authenticationService = new AuthenticationServiceImpl(jwtEncoder, jwtDecoder, authenticationManager, userRepository, emailVerificationService, oAuth2Providers, refreshTokenRepository);

        when(oAuth2Provider.authenticate(authorizationCode)).thenReturn(AuthenticationRequest.builder().username("testUser").build());
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(jwtEncoder.encode(any())).thenReturn(jwt);

        //When
        AuthenticationResponse response = authenticationService.authenticateOAuth2(provider, authorizationCode);

        //Then
        assertNotNull(response);
    }

    @DisplayName("Test authenticateOAuth2 with invalid provider")
    @Test
    public void authenticateOAuth2_withInvalidProvider_throwsException() {
        //Given
        String provider = "invalid";
        String authorizationCode = "authorizationCode";

        //When
        Exception exception = assertThrows(IllegalArgumentException.class, () -> authenticationService.authenticateOAuth2(provider, authorizationCode));

        //Then
        assertEquals("Unsupported OAuth2 provider: invalid", exception.getMessage());
    }

    @DisplayName("Test logout with valid refresh token")
    @Test
    public void shouldLogout() {
        //Given
        String username = "testUser";

        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        //When
        authenticationService.logout(username);

        //Then
        verify(refreshTokenRepository,times(1)).deleteByUser(any());
    }
}


