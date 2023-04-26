package ma.ac.inpt.authservice.exception;

/**
 * Exception thrown when an attempt is made to register a user with an email that already exists in the system.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new `EmailAlreadyExistsException` with the specified error message.
     *
     * @param message the error message
     */
    public EmailAlreadyExistsException(String message) {
        super(message);
    }

}

