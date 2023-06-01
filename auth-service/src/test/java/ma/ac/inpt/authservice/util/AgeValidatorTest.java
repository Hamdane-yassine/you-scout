package ma.ac.inpt.authservice.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.Period;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("AgeValidator Test")
class AgeValidatorTest {

    private final AgeValidator ageValidator = new AgeValidator();

    @Test
    @DisplayName("Should return true when birthdate is null")
    void testNullBirthdate() {
        // given

        // when
        boolean isValid = ageValidator.isValid(null, null);

        // then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return true when user age is above the minimum age requirement")
    void testAgeAboveMinimum() {
        // given
        LocalDate birthdate = LocalDate.now().minus(Period.ofYears(ValidAge.MINIMUM_AGE + 1));

        // when
        boolean isValid = ageValidator.isValid(birthdate, null);

        // then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false when user age is below the minimum age requirement")
    void testAgeBelowMinimum() {
        // given
        LocalDate birthdate = LocalDate.now().minus(Period.ofYears(ValidAge.MINIMUM_AGE - 1));

        // when
        boolean isValid = ageValidator.isValid(birthdate, null);

        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return true when user age is exactly the minimum age requirement")
    void testAgeEqualMinimum() {
        // given
        LocalDate birthdate = LocalDate.now().minus(Period.ofYears(ValidAge.MINIMUM_AGE));

        // when
        boolean isValid = ageValidator.isValid(birthdate, null);

        // then
        assertTrue(isValid);
    }
}

