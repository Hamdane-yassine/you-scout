package ma.ac.inpt.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
        System.out.println(message);
    }
}