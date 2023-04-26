package ma.ac.inpt.authservice.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = AuthenticationRequestValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAuthenticationRequest {

    String message() default "Authentication request in incorrect format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}



