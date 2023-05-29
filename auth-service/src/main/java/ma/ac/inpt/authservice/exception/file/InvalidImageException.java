package ma.ac.inpt.authservice.exception.file;

import ma.ac.inpt.authservice.exception.ApplicationException;

public class InvalidImageException extends ApplicationException {
    public InvalidImageException(String message) {
        super(message);
    }
}
