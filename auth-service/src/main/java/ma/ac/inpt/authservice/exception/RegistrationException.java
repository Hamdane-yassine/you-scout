package ma.ac.inpt.authservice.exception;

/**
 * RegistrationException is thrown when an error occurs during user registration.
 */
public class RegistrationException extends RuntimeException {

    /**
     * Constructs a new RegistrationException with the specified message.
     *
     * @param message the detail message.
     */
    public RegistrationException(String message) {
        super(message);
    }
}
