package ma.ac.inpt.exceptions;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String message) {
        super(message);
        System.out.println(message);
    }
}
