package ma.ac.inpt.authservice.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("ImageValidator Test")
class ImageValidatorTest {

    private final ImageValidator imageValidator = new ImageValidator();
    private final ConstraintValidatorContext context = Mockito.mock(ConstraintValidatorContext.class);

    @BeforeEach
    public void setUp() {
        Mockito.when(context.buildConstraintViolationWithTemplate(Mockito.anyString())).thenReturn(Mockito.mock(ConstraintValidatorContext.ConstraintViolationBuilder.class));
    }

    @Test
    @DisplayName("Should return false when file is null")
    void testNullFile() {
        // when
        boolean isValid = imageValidator.isValid(null, context);

        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return true when file is an image with a valid extension")
    void testValidImageFile() throws IOException {
        // given
        Path path = Paths.get("src/test/resources/testImages/test.jpg");
        byte[] content = Files.readAllBytes(path);
        MultipartFile file = new MockMultipartFile("file",
                "test.jpg", "image/jpg", content);

        // when
        boolean isValid = imageValidator.isValid(file, context);

        // then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false when file is not an image")
    void testNonImageFile() throws IOException {
        // given
        Path path = Paths.get("src/test/resources/testImages/test.txt");
        byte[] content = Files.readAllBytes(path);
        MultipartFile file = new MockMultipartFile("file",
                "test.txt", "text/plain", content);

        // when
        boolean isValid = imageValidator.isValid(file, context);

        // then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should return false when file is an image with an invalid extension")
    void testImageFileWithInvalidExtension() throws IOException {
        // given
        Path path = Paths.get("src/test/resources/testImages/test.bmp");
        byte[] content = Files.readAllBytes(path);
        MultipartFile file = new MockMultipartFile("file",
                "test.bmp", "image/bmp", content);

        // when
        boolean isValid = imageValidator.isValid(file, context);

        // then
        assertFalse(isValid);
    }
}

