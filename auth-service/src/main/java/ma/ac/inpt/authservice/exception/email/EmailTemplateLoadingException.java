package ma.ac.inpt.authservice.exception.email;

import ma.ac.inpt.authservice.exception.ApplicationException;

/**
 * Custom exception class for cases where an email template fails to load.
 */
public class EmailTemplateLoadingException extends ApplicationException {
    public EmailTemplateLoadingException(String message) {
        super(message);
    }
}