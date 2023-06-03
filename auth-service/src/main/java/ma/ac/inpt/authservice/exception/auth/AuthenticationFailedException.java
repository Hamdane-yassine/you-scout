package ma.ac.inpt.authservice.exception.auth;

import ma.ac.inpt.authservice.exception.ApplicationException;

public class AuthenticationFailedException extends ApplicationException {
    public AuthenticationFailedException(String message) {
        super(message);
    }
}
