package ma.ac.inpt.authservice.exception;


/**
 * Exception thrown when a requested role is not found in the system.
 */
public class RoleNotFoundException extends RuntimeException {

    /**
     * Constructs a new RoleNotFoundException with the specified detail message.
     * @param roleNotFound the detail message.
     */
    public RoleNotFoundException(String roleNotFound) {
        super(roleNotFound);
    }
}

