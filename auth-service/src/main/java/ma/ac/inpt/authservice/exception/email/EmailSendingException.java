package ma.ac.inpt.authservice.exception.email;

import ma.ac.inpt.authservice.exception.ApplicationException;

/**
 * Custom exception class for cases where an email fails to send.
 */
public class EmailSendingException extends ApplicationException {
    public EmailSendingException(String message) {
        super(message);
    }
}