package ma.ac.inpt.authservice.util;

import ma.ac.inpt.authservice.payload.AuthenticationRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AuthenticationRequestValidator implements ConstraintValidator<ValidAuthenticationRequest, AuthenticationRequest> {
    @Override
    public void initialize(ValidAuthenticationRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(AuthenticationRequest value, ConstraintValidatorContext context) {
        if(value.getGrantType()!=null){
            return switch (value.getGrantType()) {
                case "PASSWORD" -> validatePasswordGrantRequest(value);
                case "REFRESH_TOKEN" -> validateRefreshTokenRequest(value);
                default -> false;
            };
        }
        return false;
    }

    private boolean validatePasswordGrantRequest(AuthenticationRequest request) {
        if (request.getUsername() == null || request.getUsername().isEmpty() || request.getUsername().isBlank() || request.getPassword() == null || request.getPassword().isEmpty() || request.getPassword().isBlank()) {
            return false;
        }
        return request.getRefreshToken() == null;
    }

    private boolean validateRefreshTokenRequest(AuthenticationRequest request) {
        if (request.getRefreshToken() == null || request.getRefreshToken().isEmpty() || request.getRefreshToken().isBlank()) {
            return false;
        }
        return request.getUsername() == null && request.getPassword() == null;
    }
}
