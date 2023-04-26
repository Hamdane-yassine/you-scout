package ma.ac.inpt.authservice.exception;

public class UploadFileException extends RuntimeException {

    /**
     * Constructs a new InvalidRefreshTokenException with the specified error message.
     *
     * @param  message the error message.
     */
    public UploadFileException(String message) {
        super(message);
    }
}
