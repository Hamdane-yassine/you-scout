package ma.ac.inpt.postservice.exception;


/**
 * Custom exception class for cases where a file cannot be deleted.
 */
public class UpdatingException extends RuntimeException {
    public UpdatingException(String message) {
        super(message);
    }
}