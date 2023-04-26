package ma.ac.inpt.authservice.exception;

/**
 * Custom exception class for handling the case when a Role already exists.
 */
public class RoleAlreadyExistException extends RuntimeException {
    /**
     * Constructor that takes a message parameter to set the exception message.
     *
     * @param message the message to set for the exception
     */
    public RoleAlreadyExistException(String message) {
        super(message);
    }
}