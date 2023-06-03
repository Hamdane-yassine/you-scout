package ma.ac.inpt.authservice.exception;

import lombok.Builder;
import lombok.Data;
import ma.ac.inpt.authservice.exception.auth.AccountNotEnabledException;
import ma.ac.inpt.authservice.exception.auth.AuthenticationFailedException;
import ma.ac.inpt.authservice.exception.auth.InvalidRefreshTokenException;
import ma.ac.inpt.authservice.exception.file.DeleteFileException;
import ma.ac.inpt.authservice.exception.file.UploadFileException;
import ma.ac.inpt.authservice.exception.role.RoleAlreadyExistException;
import ma.ac.inpt.authservice.exception.role.RoleNotFoundException;
import ma.ac.inpt.authservice.exception.user.PasswordInvalidException;
import ma.ac.inpt.authservice.exception.user.UsernameAlreadyExistsException;
import ma.ac.inpt.authservice.exception.email.EmailAlreadyExistsException;
import ma.ac.inpt.authservice.exception.email.EmailSendingException;
import ma.ac.inpt.authservice.exception.email.EmailTemplateLoadingException;
import ma.ac.inpt.authservice.exception.oauth2.EmailNotVerifiedException;
import ma.ac.inpt.authservice.exception.oauth2.GoogleAuthorizationCodeTokenRequestException;
import ma.ac.inpt.authservice.exception.registration.InvalidRequestException;
import ma.ac.inpt.authservice.exception.registration.RegistrationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller advice class that handles exceptions thrown and returns custom error responses.
 */
@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    /**
     * Handles authentication-related exceptions, such as when a user provides invalid credentials or an expired refresh token.
     * Returns an HTTP 401 UNAUTHORIZED response with a custom error message.
     */
    @ExceptionHandler({AuthenticationFailedException.class,InvalidRefreshTokenException.class, AccountNotEnabledException.class, EmailNotVerifiedException.class, PasswordInvalidException.class})
    public ResponseEntity<Object> handleAuthenticationException(ApplicationException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    /**
     * Handles exceptions when a requested resource is not found in the system, such as when a requested user or role does not exist.
     * Returns an HTTP 404 NOT FOUND response with a custom error message.
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(UsernameNotFoundException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /**
     * Handles exceptions when a requested resource is not found in the system, such as when a requested user or role does not exist.
     * Returns an HTTP 404 NOT FOUND response with a custom error message.
     */
    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(ApplicationException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /**
     * Handles exceptions when a user or role already exists in the system, such as when attempting to register a user with an existing username.
     * Returns an HTTP 409 CONFLICT response with a custom error message.
     */
    @ExceptionHandler({UsernameAlreadyExistsException.class, EmailAlreadyExistsException.class, RoleAlreadyExistException.class})
    public ResponseEntity<Object> handleConflictException(ApplicationException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * Handles exceptions when a request is invalid, such as when missing required request parameters.
     * Returns an HTTP 400 BAD REQUEST response with a custom error message.
     */
    @ExceptionHandler({InvalidRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(ApplicationException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Handles exceptions when an internal server error occurs, such as when an email fails to send or an error occurs during file upload.
     * Returns an HTTP 500 INTERNAL SERVER ERROR response with a custom error message.
     */
    @ExceptionHandler({RegistrationException.class, UploadFileException.class, DeleteFileException.class, EmailSendingException.class, EmailTemplateLoadingException.class, GoogleAuthorizationCodeTokenRequestException.class})
    public ResponseEntity<Object> handleInternalServerErrorException(ApplicationException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    /**
     * Overrides the default handler for MethodArgumentNotValidException errors, which occur when a request fails validation.
     * Returns an HTTP 400 BAD REQUEST response with a custom error message detailing the validation errors.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        // Build custom error response body
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("path", request.getDescription(false));

        // Get error message from validation errors
        String errorMessage = ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).findFirst().orElse("Validation error");
        body.put("message", errorMessage);

        return new ResponseEntity<>(body, headers, status);
    }

    /**
     * Builds a custom response entity with the given HTTP status, error message, and web request description.
     */
    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message, WebRequest request) {
        // Build custom error response body
        CustomErrorResponse errorResponse = CustomErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getDescription(false).substring(4)) // removing "uri=" prefix
                .build();
        // Return response entity with custom error body and HTTP status
        return new ResponseEntity<>(errorResponse, status);
    }

}

/**
 * Custom error response body with timestamp, HTTP status code, error message, and the path of the request that caused the error.
 */
@Data
@Builder
class CustomErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}

