package ma.ac.inpt.socialgraphservice.exception;

public class UserOperationNotAllowedException extends RuntimeException {
    public UserOperationNotAllowedException(String message) {
        super(message);
    }
}

