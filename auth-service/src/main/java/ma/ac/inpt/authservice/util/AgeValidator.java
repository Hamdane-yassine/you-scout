package ma.ac.inpt.authservice.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

/**
 * AgeValidator is a custom constraint validator that implements the ConstraintValidator interface.
 * It validates a user's age by checking if it meets the minimum age requirement specified in the
 * ValidAge annotation.
 */
public class AgeValidator implements ConstraintValidator<ValidAge, LocalDate> {

    /**
     * Initializes the constraint validator.
     *
     * @param constraintAnnotation the ValidAge constraint annotation
     */
    @Override
    public void initialize(ValidAge constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    /**
     * Validates if the provided birthdate meets the minimum age requirement.
     *
     * @param birthdate the LocalDate representing the user's birthdate
     * @param context   the constraint validator context
     * @return true if the birthdate meets the minimum age requirement, otherwise false
     */
    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext context) {
        if (birthdate == null) {
            return true;
        }

        LocalDate now = LocalDate.now();
        Period age = Period.between(birthdate, now);

        return age.getYears() >= ValidAge.MINIMUM_AGE;
    }
}


