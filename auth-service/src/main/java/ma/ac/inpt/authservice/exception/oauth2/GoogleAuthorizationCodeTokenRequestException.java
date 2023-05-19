package ma.ac.inpt.authservice.exception.oauth2;

import ma.ac.inpt.authservice.exception.ApplicationException;

/**
 * Custom exception class for cases where there is an error with Google authorization code token requests.
 */
public class GoogleAuthorizationCodeTokenRequestException extends ApplicationException {
    public GoogleAuthorizationCodeTokenRequestException(String message) {
        super(message);
    }
}