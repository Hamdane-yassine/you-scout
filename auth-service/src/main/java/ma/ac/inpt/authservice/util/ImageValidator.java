package ma.ac.inpt.authservice.util;

import org.apache.tika.Tika;
import org.apache.tika.mime.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    // Allowable media types for images
    private static final Set<MediaType> ALLOWED_MIME_TYPES = new HashSet<>(
            Arrays.asList(
                    MediaType.image("png"),
                    MediaType.image("jpeg"),
                    MediaType.image("jpg")));
    // The Apache Tika object to analyze file content
    private final Tika tika;

    public ImageValidator() {
        this.tika = new Tika();
    }

    @Override
    public void initialize(ValidImage constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        // Return false if the file is null or empty
        if (file == null || file.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("File must not be null or empty").addConstraintViolation();
            return false;
        }

        try {
            String detectedType = tika.detect(file.getBytes());
            MediaType mediaType = MediaType.parse(detectedType);
            // If not a valid image type, report the error with a detailed message
            if (!ALLOWED_MIME_TYPES.contains(mediaType)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Invalid image file. Accepted types are jpg, jpeg, png").addConstraintViolation();
                return false;
            }
            return true;
        } catch (IOException e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid image file format").addConstraintViolation();
            return false;
        }
    }
}

