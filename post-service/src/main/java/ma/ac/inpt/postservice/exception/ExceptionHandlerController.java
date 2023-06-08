package ma.ac.inpt.postservice.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

/**
 * Controller advice class that handles exceptions thrown and returns custom error responses.
 */
@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {


    /**
     * Handles exceptions when a requested resource is not found in the system, such as when a requested user or role does not exist.
     * Returns an HTTP 404 NOT FOUND response with a custom error message.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }


    /**
     * Handles exceptions when a user or role already exists in the system, such as when attempting to register a user with an existing username.
     * Returns an HTTP 409 CONFLICT response with a custom error message.
     */
    @ExceptionHandler({VideoValidationException.class,})
    public ResponseEntity<Object> handleConflictException(VideoValidationException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    /**
     * Handles exceptions when a request is invalid, such as when missing required request parameters.
     * Returns an HTTP 400 BAD REQUEST response with a custom error message.
     */
    @ExceptionHandler({InvalidRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(InvalidRequestException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    /**
     * Handles exceptions when an internal server error occurs, such as when an email fails to send or an error occurs during file upload.
     * Returns an HTTP 500 INTERNAL SERVER ERROR response with a custom error message.
     */
    @ExceptionHandler({UploadFileException.class, DeleteFileException.class, VideoProcessingException.class, UpdatingException.class})
    public ResponseEntity<Object> handleInternalServerErrorException(RuntimeException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
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

@Data
@Builder
class CustomErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
