package ma.ac.inpt.socialgraphservice.exception;

/**
 * Exception class indicating that a user already exists.
 */
public class UserAlreadyExistsException extends RuntimeException {

    /**
     * Constructs a new UserAlreadyExistsException with the specified detail message.
     *
     * @param message the detail message explaining the exception
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}