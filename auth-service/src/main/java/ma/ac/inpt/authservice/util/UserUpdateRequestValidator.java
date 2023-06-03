package ma.ac.inpt.authservice.util;

import ma.ac.inpt.authservice.dto.UserUpdateRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * UserUpdateRequestValidator is a custom constraint validator for validating user update requests.
 */
public class UserUpdateRequestValidator implements ConstraintValidator<ValidUserUpdateRequest, UserUpdateRequest> {

    /**
     * Initializes the UserUpdateRequestValidator with the constraint annotation.
     *
     * @param constraintAnnotation the ValidUserUpdateRequest constraint annotation
     */
    @Override
    public void initialize(ValidUserUpdateRequest constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates the user update request.
     *
     * @param value   the user update request to be validated
     * @param context the constraint validator context
     * @return true if the user update request is valid, false otherwise
     */
    @Override
    public boolean isValid(UserUpdateRequest value, ConstraintValidatorContext context) {
        return value.getPassword() != null && (value.getNewPassword() != null || value.getNewUsername() != null || value.getNewEmail() != null);
    }
}
