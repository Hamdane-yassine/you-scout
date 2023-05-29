package ma.ac.inpt.authservice.util;

import ma.ac.inpt.authservice.dto.AuthenticationRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * AuthenticationRequestValidator is a custom ConstraintValidator that validates
 * authentication requests based on the ValidAuthenticationRequest annotation.
 * It checks if the request meets the defined constraints based on the grant type
 * and ensures that the request contains the necessary fields.
 */
public class AuthenticationRequestValidator implements ConstraintValidator<ValidAuthenticationRequest, AuthenticationRequest> {

    @Override
    public void initialize(ValidAuthenticationRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the authentication request based on the grant type.
     *
     * @param value   the authentication request object to validate
     * @param context the constraint validator context
     * @return true if the request is valid, false otherwise
     */
    @Override
    public boolean isValid(AuthenticationRequest value, ConstraintValidatorContext context) {
        if (value.getGrantType() != null) {
            String grantType = value.getGrantType().toUpperCase();
            return switch (grantType) {
                case "PASSWORD" -> validatePasswordGrantRequest(value);
                case "REFRESH_TOKEN" -> validateRefreshTokenRequest(value);
                default -> false;
            };
        }
        return false;
    }

    /**
     * Validates the password grant request by checking if the username and password fields are present.
     *
     * @param request the authentication request object
     * @return true if the password grant request is valid, false otherwise
     */
    private boolean validatePasswordGrantRequest(AuthenticationRequest request) {
        if (request.getUsername() == null || request.getUsername().isBlank() ||
                request.getPassword() == null || request.getPassword().isBlank()) {
            return false;
        }
        return request.getRefreshToken() == null;
    }

    /**
     * Validates the refresh token request by checking if the refresh token field is present
     * and username and password fields are not present.
     *
     * @param request the authentication request object
     * @return true if the refresh token request is valid, false otherwise
     */
    private boolean validateRefreshTokenRequest(AuthenticationRequest request) {
        if (request.getRefreshToken() == null || request.getRefreshToken().isBlank()) {
            return false;
        }
        return request.getUsername() == null && request.getPassword() == null;
    }
}

