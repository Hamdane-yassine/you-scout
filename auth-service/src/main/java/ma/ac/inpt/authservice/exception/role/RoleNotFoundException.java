package ma.ac.inpt.authservice.exception.role;


import ma.ac.inpt.authservice.exception.ApplicationException;

/**
 * Custom exception class for cases where a role is not found in the system.
 */
public class RoleNotFoundException extends ApplicationException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}

