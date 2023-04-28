package ma.ac.inpt.authservice.exception.oauth2;

import ma.ac.inpt.authservice.exception.ApplicationException;

/**
 * Custom exception class for cases where a user's email has not been verified.
 */
public class EmailNotVerifiedException extends ApplicationException {
    public EmailNotVerifiedException(String message) {
        super(message);
    }
}
