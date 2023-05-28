package ma.ac.inpt.authservice.util;

import ma.ac.inpt.authservice.dto.AuthenticationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("AuthenticationRequestValidator Test")
class AuthenticationRequestValidatorTest {

    private final AuthenticationRequestValidator requestValidator = new AuthenticationRequestValidator();

    @Test
    @DisplayName("Should return false when grant type is null")
    void testNullGrantType() {
        // given
        AuthenticationRequest request = AuthenticationRequest.builder()
                .grantType(null)
                .username("username")
                .password("password")
                .withRefreshToken(false)
                .refreshToken("refreshToken")
                .build();

        // when
        boolean isValid = requestValidator.isValid(request, null);

        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return true for valid PASSWORD grant type request")
    void testValidPasswordGrantRequest() {
        // given
        AuthenticationRequest request = AuthenticationRequest.builder()
                .grantType("PASSWORD")
                .username("username")
                .password("password")
                .withRefreshToken(false)
                .refreshToken(null)
                .build();

        // when
        boolean isValid = requestValidator.isValid(request, null);

        // then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false for invalid PASSWORD grant type request")
    void testInvalidPasswordGrantRequest() {
        // given
        AuthenticationRequest request = AuthenticationRequest.builder()
                .grantType("PASSWORD")
                .username("")
                .password("")
                .withRefreshToken(true)
                .refreshToken("refreshToken")
                .build();

        // when
        boolean isValid = requestValidator.isValid(request, null);

        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return true for valid REFRESH_TOKEN grant type request")
    void testValidRefreshTokenRequest() {
        // given
        AuthenticationRequest request = AuthenticationRequest.builder()
                .grantType("REFRESH_TOKEN")
                .username(null)
                .password(null)
                .withRefreshToken(false)
                .refreshToken("refreshToken")
                .build();

        // when
        boolean isValid = requestValidator.isValid(request, null);

        // then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false for invalid REFRESH_TOKEN grant type request")
    void testInvalidRefreshTokenRequest() {
        // given
        AuthenticationRequest request = AuthenticationRequest.builder()
                .grantType("REFRESH_TOKEN")
                .username("username")
                .password("password")
                .withRefreshToken(false)
                .refreshToken("")
                .build();

        // when
        boolean isValid = requestValidator.isValid(request, null);

        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false for unsupported grant type")
    void testUnsupportedGrantType() {
        // given
        AuthenticationRequest request = AuthenticationRequest.builder()
                .grantType("UNSUPPORTED")
                .username("username")
                .password("password")
                .withRefreshToken(true)
                .refreshToken("refreshToken")
                .build();

        // when
        boolean isValid = requestValidator.isValid(request, null);

        // then
        assertFalse(isValid);
    }
}


