package ma.ac.inpt.authservice.exception.auth;

import ma.ac.inpt.authservice.exception.ApplicationException;

/**
 * Custom exception class for cases where the user's account is not enabled.
 */
public class AccountNotEnabledException extends ApplicationException {
    public AccountNotEnabledException(String message) {
        super(message);
    }
}