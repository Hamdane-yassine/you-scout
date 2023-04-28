package ma.ac.inpt.authservice.exception.registration;

import ma.ac.inpt.authservice.exception.ApplicationException;

/**
 * Custom exception class for cases where there is an error with user registration.
 */
public class RegistrationException extends ApplicationException {
    public RegistrationException(String message) {
        super(message);
    }
}
