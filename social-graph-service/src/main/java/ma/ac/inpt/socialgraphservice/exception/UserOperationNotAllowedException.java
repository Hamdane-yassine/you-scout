package ma.ac.inpt.socialgraphservice.exception;

/**
 * Exception class indicating that a user operation is not allowed.
 */
public class UserOperationNotAllowedException extends RuntimeException {

    /**
     * Constructs a new UserOperationNotAllowedException with the specified detail message.
     *
     * @param message the detail message explaining the exception
     */
    public UserOperationNotAllowedException(String message) {
        super(message);
    }
}

