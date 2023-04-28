package ma.ac.inpt.authservice.exception.auth;

import ma.ac.inpt.authservice.exception.ApplicationException;

/**
 * Custom exception class for cases where a refresh token is invalid.
 */
public class InvalidRefreshTokenException extends ApplicationException {
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
