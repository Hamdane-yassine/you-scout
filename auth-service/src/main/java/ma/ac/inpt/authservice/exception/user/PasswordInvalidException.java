package ma.ac.inpt.authservice.exception.user;

import ma.ac.inpt.authservice.exception.ApplicationException;

public class PasswordInvalidException extends ApplicationException {
    public PasswordInvalidException(String message) {
        super(message);
    }
}
