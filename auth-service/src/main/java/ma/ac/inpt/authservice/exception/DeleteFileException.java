package ma.ac.inpt.authservice.exception;

public class DeleteFileException extends RuntimeException {

    /**
     * Constructs a new InvalidRefreshTokenException with the specified error message.
     *
     * @param  message the error message.
     */
    public DeleteFileException(String message) {
        super(message);
    }
}