package ma.ac.inpt.authservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler that catches all exceptions thrown by the API
 * and maps them to appropriate HTTP response codes and error messages.
 */
@ControllerAdvice
public class ExceptionHandlerController {

    /**
     * Handles EmailAlreadyExistsException and returns a conflict response.
     *
     * @param ex the EmailAlreadyExistsException to handle
     * @return a ResponseEntity with HTTP status code 409 and the exception message in the body
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Handles InvalidRefreshTokenException and returns an unauthorized response.
     *
     * @param ex the InvalidRefreshTokenException to handle
     * @return a ResponseEntity with HTTP status code 401 and the exception message in the body
     */
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<String> handleInvalidRefreshTokenException(InvalidRefreshTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    /**
     * Handles InvalidRequestException and returns a bad request response.
     *
     * @param ex the InvalidRequestException to handle
     * @return a ResponseEntity with HTTP status code 400 and the exception message in the body
     */
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<String> handleInvalidRequestException(InvalidRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Handles RegistrationException and returns an internal server error response.
     *
     * @param ex the RegistrationException to handle
     * @return a ResponseEntity with HTTP status code 500 and the exception message in the body
     */
    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> handleRegistrationException(RegistrationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    /**
     * Handles UsernameAlreadyExistsException and returns a conflict response.
     *
     * @param ex the UsernameAlreadyExistsException to handle
     * @return a ResponseEntity with HTTP status code 409 and the exception message in the body
     */
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<String> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Handles UsernameNotFoundException and RoleNotFoundException and returns a not found response.
     *
     * @param ex the exception to handle
     * @return a ResponseEntity with HTTP status code 404 and the exception message in the body
     */
    @ExceptionHandler({UsernameNotFoundException.class, RoleNotFoundException.class})
    public ResponseEntity<String> RoleOrUserNotFoundException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles RoleAlreadyExistException and returns a conflict response.
     *
     * @param ex the RoleAlreadyExistException to handle
     * @return a ResponseEntity with HTTP status code 409 and the exception message in the body
     */
    @ExceptionHandler(RoleAlreadyExistException.class)
    public ResponseEntity<String> handleRoleAlreadyExistException(RoleAlreadyExistException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
