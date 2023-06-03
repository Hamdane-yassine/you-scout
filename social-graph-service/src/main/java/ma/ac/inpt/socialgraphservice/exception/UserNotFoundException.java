package ma.ac.inpt.socialgraphservice.exception;


/**
 * Exception class indicating that a user was not found.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining the exception
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}

