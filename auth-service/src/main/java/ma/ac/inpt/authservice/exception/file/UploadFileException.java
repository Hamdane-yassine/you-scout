package ma.ac.inpt.authservice.exception.file;

import ma.ac.inpt.authservice.exception.ApplicationException;

/**
 * Custom exception class for cases where a file cannot be uploaded.
 */
public class UploadFileException extends ApplicationException {
    public UploadFileException(String message) {
        super(message);
    }
}
