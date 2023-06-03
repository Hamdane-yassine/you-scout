package ma.ac.inpt.postservice.exception;


/**
 * Custom exception class for cases where a file cannot be uploaded.
 */
public class UploadFileException extends RuntimeException {
    public UploadFileException(String message) {
        super(message);
    }
}
