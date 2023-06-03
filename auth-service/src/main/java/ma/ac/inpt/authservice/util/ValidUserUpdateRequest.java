package ma.ac.inpt.authservice.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * This annotation represents a custom constraint to validate a user update request.
 * It's applied to a UserUpdateRequest to ensure that it's in the correct format.
 */
@Documented
@Constraint(validatedBy = UserUpdateRequestValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserUpdateRequest {

    String message() default "User update request is in incorrect format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
