package ma.ac.inpt.authservice.exception.registration;

import ma.ac.inpt.authservice.exception.ApplicationException;

/**
 * Custom exception class for cases where a request is invalid.
 */
public class InvalidRequestException extends ApplicationException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
