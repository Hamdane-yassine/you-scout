package ma.ac.inpt.authservice.exception.role;


import ma.ac.inpt.authservice.exception.ApplicationException;

/**
 * Custom exception class for cases where a role already exists in the system.
 */
public class RoleAlreadyExistException extends ApplicationException {
    public RoleAlreadyExistException(String message) {
        super(message);
    }
}