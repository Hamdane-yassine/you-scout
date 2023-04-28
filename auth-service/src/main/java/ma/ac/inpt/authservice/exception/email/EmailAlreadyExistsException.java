package ma.ac.inpt.authservice.exception.email;

import ma.ac.inpt.authservice.exception.ApplicationException;

/**
 * Custom exception class for cases where an email already exists in the system.
 */
public class EmailAlreadyExistsException extends ApplicationException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}

