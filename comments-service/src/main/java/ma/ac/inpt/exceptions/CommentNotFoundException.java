package ma.ac.inpt.exceptions;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String message) {
        super(message);
        System.out.println(message);
    }
}