package ma.ac.inpt.authservice.exception.user;


import ma.ac.inpt.authservice.exception.ApplicationException;

/**
 * Custom exception class for cases where a username already exists in the system.
 */
public class UsernameAlreadyExistsException extends ApplicationException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}

