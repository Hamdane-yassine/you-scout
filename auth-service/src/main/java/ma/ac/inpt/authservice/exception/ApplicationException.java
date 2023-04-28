package ma.ac.inpt.authservice.exception;

/**
 * Base class for all application exceptions.
 */
public class ApplicationException extends RuntimeException {
    public ApplicationException(String message) {
        super(message);
    }
}
