package ma.ac.inpt.authservice.exception.user;

import ma.ac.inpt.authservice.exception.ApplicationException;

/**
 * Custom exception class for cases where a user is not found in the system.
 */
public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
