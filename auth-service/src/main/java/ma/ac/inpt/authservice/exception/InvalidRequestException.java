package ma.ac.inpt.authservice.exception;

/**
 * Exception thrown when an invalid request is received by the system.
 */
public class InvalidRequestException extends RuntimeException {

    /**
     * Constructs a new InvalidRequestException with the specified message.
     *
     * @param message a String containing the message that describes the error
     */
    public InvalidRequestException(String message) {
        super(message);
    }
}
