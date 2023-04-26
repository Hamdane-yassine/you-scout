package ma.ac.inpt.authservice.exception;

public class EmailSendingException extends RuntimeException {

    /**
     * Constructs a new InvalidRefreshTokenException with the specified error message.
     *
     * @param  message the error message.
     */
    public EmailSendingException(String message) {
        super(message);
    }
}