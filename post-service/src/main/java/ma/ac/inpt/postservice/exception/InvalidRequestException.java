package ma.ac.inpt.postservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_FOUND)
public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String resource) {
        super(String.format("Resource %s not found", resource));
    }
}