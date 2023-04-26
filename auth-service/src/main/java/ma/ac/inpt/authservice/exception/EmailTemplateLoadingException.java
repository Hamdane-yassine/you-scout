package ma.ac.inpt.authservice.exception;

public class EmailTemplateLoadingException extends RuntimeException {

    /**
     * Constructs a new InvalidRefreshTokenException with the specified error message.
     *
     * @param  message the error message.
     */
    public EmailTemplateLoadingException(String message) {
        super(message);
    }
}