package ma.ac.inpt.authservice.service.auth;

import ma.ac.inpt.authservice.dto.ForgotPasswordRequest;
import ma.ac.inpt.authservice.dto.ResetPasswordRequest;
import ma.ac.inpt.authservice.model.PasswordResetToken;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.repository.PasswordResetTokenRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordResetServiceTest {

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private ApplicationBaseUrlRetriever applicationBaseUrlRetriever;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VerificationTokenRepository verificationTokenRepository;

    @InjectMocks
    private PasswordResetServiceImpl passwordResetService;

    private User testUser;
    private PasswordResetToken testToken;

    @BeforeEach
    void setUp() {
        testUser = User.builder().
                username("testUsername").
                email("test@test.com").
                password(passwordEncoder.encode("testPassword")).
                build();

        testToken = PasswordResetToken.builder().
                user(testUser).token("testToken").
                expiryDate(LocalDateTime.now().plusDays(1)).
                build();
    }

    @DisplayName("Test Send Password Reset Email")
    @Test
    void sendPasswordResetEmail() {
        // Given
        ForgotPasswordRequest request = ForgotPasswordRequest.builder().email(testUser.getEmail()).build();

        when(userRepository.findByEmailIgnoreCase(any())).thenReturn(Optional.of(testUser));
        when(passwordResetTokenRepository.save(any())).thenReturn(testToken); // Added this line

        // When
        passwordResetService.sendPasswordResetEmail(request);

        // Then
        verify(emailService, times(1)).sendEmail(any());
        verify(passwordResetTokenRepository, times(1)).save(any());
    }

    @DisplayName("Test Reset Password")
    @Test
    void resetPassword() {
        // Given
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setNewPassword("newPassword");

        when(passwordResetTokenRepository.findByToken(any())).thenReturn(Optional.of(testToken));

        // When
        String result = passwordResetService.resetPassword(request, "testToken");

        // Then
        assertEquals("Your operation has been successfully completed!", result);
        verify(userRepository, times(1)).save(any());
    }
}


