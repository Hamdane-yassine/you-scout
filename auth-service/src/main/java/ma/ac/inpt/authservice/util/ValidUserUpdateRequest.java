package ma.ac.inpt.authservice.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserUpdateRequestValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserUpdateRequest {

    String message() default "user update request is in incorrect format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
