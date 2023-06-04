package ma.ac.inpt.postservice.exception;


/**
 * Custom exception class for cases where a file cannot be deleted.
 */
public class DeleteFileException extends RuntimeException {
    public DeleteFileException(String message) {
        super(message);
    }
}