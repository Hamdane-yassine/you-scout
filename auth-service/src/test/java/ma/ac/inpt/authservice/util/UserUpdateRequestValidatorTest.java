package ma.ac.inpt.authservice.util;

import ma.ac.inpt.authservice.dto.UserUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("UserUpdateRequestValidator Test")
class UserUpdateRequestValidatorTest {

    private final UserUpdateRequestValidator requestValidator = new UserUpdateRequestValidator();

    @Test
    @DisplayName("Should return false when password is null")
    void testNullPassword() {
        // given
        UserUpdateRequest request = new UserUpdateRequest(
                "email@example.com",
                "username",
                null,
                "NewPassword1@"
        );

        // when
        boolean isValid = requestValidator.isValid(request, null);

        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false when newPassword, username and email are null but password is present")
    void testPasswordPresentOthersNull() {
        // given
        UserUpdateRequest request = new UserUpdateRequest(
                null,
                null,
                "Password1@",
                null
        );

        // when
        boolean isValid = requestValidator.isValid(request, null);

        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return true when password and newPassword are present but username and email are null")
    void testPasswordAndNewPasswordPresentOthersNull() {
        // given
        UserUpdateRequest request = new UserUpdateRequest(
                null,
                null,
                "Password1@",
                "NewPassword1@"
        );

        // when
        boolean isValid = requestValidator.isValid(request, null);

        // then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return true when password and username are present but newPassword and email are null")
    void testPasswordAndUsernamePresentOthersNull() {
        // given
        UserUpdateRequest request = new UserUpdateRequest(
                null,
                "username",
                "Password1@",
                null
        );

        // when
        boolean isValid = requestValidator.isValid(request, null);

        // then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return true when password and email are present but newPassword and username are null")
    void testPasswordAndEmailPresentOthersNull() {
        // given
        UserUpdateRequest request = new UserUpdateRequest(
                "email@example.com",
                null,
                "Password1@",
                null
        );

        // when
        boolean isValid = requestValidator.isValid(request, null);

        // then
        assertTrue(isValid);
    }
}

