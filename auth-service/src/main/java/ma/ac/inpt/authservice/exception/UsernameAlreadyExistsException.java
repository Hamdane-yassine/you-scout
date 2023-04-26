package ma.ac.inpt.authservice.exception;

/**
 * An exception to be thrown when a username already exists in the system.
 */
public class UsernameAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new UsernameAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message.
     */
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }

}

