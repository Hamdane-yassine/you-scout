package ma.ac.inpt.authservice.util;

import org.apache.tika.Tika;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

public class ImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

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
        if (file == null || file.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("The file must not be null or empty").addConstraintViolation();
            return false;
        }

        String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        if (fileExtension == null || !isAllowedExtension(fileExtension, file)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Invalid image file. Accepted types are jpg, jpeg, png, gif").addConstraintViolation();
            return false;
        }

        return true;
    }


    private boolean isAllowedExtension(String fileExtension, MultipartFile file) {
        String detectedType;
        try {
            detectedType = tika.detect(file.getBytes()).toLowerCase();
        } catch (IOException e) {
            return false;
        }

        return switch (fileExtension.toLowerCase()) {
            case "jpg", "jpeg" -> detectedType.equals("image/jpeg");
            case "png" -> detectedType.equals("image/png");
            case "gif" -> detectedType.equals("image/gif");
            default -> false;
        };
    }

}

