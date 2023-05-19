package ma.ac.inpt.authservice.util;

import ma.ac.inpt.authservice.dto.UserUpdateRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserUpdateRequestValidator implements ConstraintValidator<ValidUserUpdateRequest, UserUpdateRequest> {
    @Override
    public void initialize(ValidUserUpdateRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(UserUpdateRequest value, ConstraintValidatorContext context) {
        return value.getPassword() != null && (value.getNewPassword() != null || value.getUsername() != null || value.getEmail() != null);
    }
}
