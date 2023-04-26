package ma.ac.inpt.authservice.exception;

public class BaseUrlNotResolvedException extends RuntimeException {

    /**
     * Constructs a new InvalidRefreshTokenException with the specified error message.
     *
     * @param  message the error message.
     */
    public BaseUrlNotResolvedException(String message) {
        super(message);
    }
}
