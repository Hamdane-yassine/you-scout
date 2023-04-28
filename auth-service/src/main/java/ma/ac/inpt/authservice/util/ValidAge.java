package ma.ac.inpt.authservice.util;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * ValidAge is a custom constraint annotation that validates a user's age.
 * It checks if the user is at least the specified minimum age. The validation
 * is performed using the AgeValidator class.
 */
@Documented
@Constraint(validatedBy = AgeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAge {

    int MINIMUM_AGE = 12;

    /**
     * The default error message when the age is below the minimum.
     *
     * @return the error message
     */
    String message() default "Must be at least " + MINIMUM_AGE + " years old";

    /**
     * Validation groups. Can be used to group multiple constraints.
     *
     * @return an array of classes
     */
    Class<?>[] groups() default {};

    /**
     * Payload for additional metadata.
     *
     * @return an array of payload classes
     */
    Class<? extends Payload>[] payload() default {};
}



