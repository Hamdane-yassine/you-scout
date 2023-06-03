package ma.ac.inpt.authservice.service.email;

import ma.ac.inpt.authservice.dto.EmailPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        // Set up the injected values
        ReflectionTestUtils.setField(emailService, "templatePath", "templates/test");
        ReflectionTestUtils.setField(emailService, "sender", "test@example.com");
    }

    @DisplayName("Test Send Email")
    @Test
    void sendEmail() {
        // Given
        EmailPayload payload = EmailPayload.builder().recipientAddress("test@example.com").subject("Test Email").content("This is a test email.").build();

        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // When
        assertDoesNotThrow(() -> emailService.sendEmail(payload));

        // Then
        verify(javaMailSender, times(1)).send(any(MimeMessage.class));
    }
}
