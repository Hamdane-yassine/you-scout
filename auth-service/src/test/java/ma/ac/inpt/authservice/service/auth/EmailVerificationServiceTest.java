package ma.ac.inpt.authservice.service.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ma.ac.inpt.authservice.dto.EmailVerificationType;
import ma.ac.inpt.authservice.messaging.UserEventMessagingService;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.model.VerificationToken;
import ma.ac.inpt.authservice.repository.UserRepository;
import ma.ac.inpt.authservice.repository.VerificationTokenRepository;
import ma.ac.inpt.authservice.service.email.EmailService;
import ma.ac.inpt.authservice.util.ApplicationBaseUrlRetriever;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Email Verification Service Test")
class EmailVerificationServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEventMessagingService userEventSender;

    @Mock
    private ApplicationBaseUrlRetriever applicationBaseUrlRetriever;

    @InjectMocks
    private EmailVerificationServiceImpl emailVerificationService;

    private User testUser;
    private VerificationToken testToken;

    @BeforeEach
    @DisplayName("Setup for each test")
    void setup() {
        testUser = User.builder().username("testUsername").email("test@test.com").build();

        testToken = VerificationToken.builder().
                user(testUser).token("testToken").
                expiryDate(LocalDateTime.now().plusDays(1)).
                emailVerificationType(EmailVerificationType.REGISTRATION). // Set the EmailVerificationType
                        build();
    }

    @Test
    @DisplayName("Test Send Verification Email")
    void testSendVerificationEmail() {
        // Given
        when(verificationTokenRepository.findByUser(any())).thenReturn(Optional.empty());
        when(verificationTokenRepository.save(any())).thenReturn(testToken);

        // When
        emailVerificationService.sendVerificationEmail(testUser, EmailVerificationType.REGISTRATION);

        // Then
        verify(emailService, times(1)).sendEmail(any());
    }

    @Test
    @DisplayName("Test Verify Account")
    void testVerifyAccount() {
        // Given
        when(verificationTokenRepository.findByToken(any())).thenReturn(Optional.of(testToken));

        // When
        String result = emailVerificationService.verifyAccount("testToken");

        // Then
        assertEquals("Your operation has been successfully completed!", result);
        verify(userRepository, times(1)).save(any());
        verify(userEventSender, times(1)).sendUserCreated(any());
    }
}

