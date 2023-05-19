package ma.ac.inpt.authservice.exception.file;

import ma.ac.inpt.authservice.exception.ApplicationException;

/**
 * Custom exception class for cases where a file cannot be deleted.
 */
public class DeleteFileException extends ApplicationException {
    public DeleteFileException(String message) {
        super(message);
    }
}