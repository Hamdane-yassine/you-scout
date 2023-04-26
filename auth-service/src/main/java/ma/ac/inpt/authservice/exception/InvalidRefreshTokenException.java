package ma.ac.inpt.authservice.exception;

/**
 * Exception thrown when an invalid refresh token is provided.
 */
public class InvalidRefreshTokenException extends RuntimeException {

    /**
     * Constructs a new InvalidRefreshTokenException with the specified error message.
     *
     * @param  message the error message.
     */
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
