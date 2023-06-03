package ma.ac.inpt.socialgraphservice.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for handling specific exceptions in the application.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Exception handler for handling UserNotFoundException.
     *
     * @param ex      the UserNotFoundException that occurred
     * @param request the web request
     * @return a ResponseEntity with the error message and status code
     */
    @ExceptionHandler(value = {UserNotFoundException.class})
    protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Exception handler for handling UserAlreadyExistsException.
     *
     * @param ex      the UserAlreadyExistsException that occurred
     * @param request the web request
     * @return a ResponseEntity with the error message and status code
     */
    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    protected ResponseEntity<Object> handleUserAlreadyExistException(UserAlreadyExistsException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    /**
     * Exception handler for handling UserOperationNotAllowedException.
     *
     * @param ex      the UserOperationNotAllowedException that occurred
     * @param request the web request
     * @return a ResponseEntity with the error message and status code
     */
    @ExceptionHandler(value = {UserOperationNotAllowedException.class})
    protected ResponseEntity<Object> handleUserOperationNotAllowedException(UserOperationNotAllowedException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }
}