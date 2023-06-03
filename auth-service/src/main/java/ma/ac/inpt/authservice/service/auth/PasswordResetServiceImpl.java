package ma.ac.inpt.authservice.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.dto.EmailVerificationType;
import ma.ac.inpt.authservice.exception.registration.InvalidRequestException;
import ma.ac.inpt.authservice.repository.PasswordResetTokenRepository;
import ma.ac.inpt.authservice.repository.TokenRepository;
import ma.ac.inpt.authservice.repository.UserRepository;
import ma.ac.inpt.authservice.repository.VerificationTokenRepository;
import ma.ac.inpt.authservice.service.email.EmailService;
import ma.ac.inpt.authservice.util.ApplicationBaseUrlRetriever;
import ma.ac.inpt.authservice.model.PasswordResetToken;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.dto.ForgotPasswordRequest;
import ma.ac.inpt.authservice.dto.ResetPasswordRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PasswordResetServiceImpl extends AbstractTokenService<PasswordResetToken> implements PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    private final ApplicationBaseUrlRetriever applicationBaseUrlRetriever;

    /**
     * Returns the email service used by this service.
     *
     * @return the email service used by this service
     */
    @Override
    protected EmailService getEmailService() {
        return emailService;
    }

    /**
     * Returns the password reset token repository used by this service.
     *
     * @return the password reset token repository used by this service
     */
    @Override
    protected TokenRepository<PasswordResetToken> getTokenRepository() {
        return passwordResetTokenRepository;
    }

    /**
     * Returns the user associated with the provided password reset token.
     *
     * @param token the password reset token
     * @return the user associated with the provided password reset token
     */
    @Override
    protected User getUserFromToken(PasswordResetToken token) {
        return token.getUser();
    }

    /**
     * Returns the content of the provided password reset token.
     *
     * @param token the password reset token
     * @return the content of the provided password reset token
     */
    @Override
    protected String getTokenContent(PasswordResetToken token) {
        return applicationBaseUrlRetriever.getBaseUrl() + "/api/v1/auth/password/reset?token=" + token.getToken();
    }

    /**
     * Handles a valid password reset token by resetting the password of the associated user and deleting the associated verification token (if any).
     *
     * @param user  the user associated with the password reset token
     * @param value the new password to set for the user
     */
    @Override
    protected void handleValidToken(User user, String value) {
        user.setPassword(passwordEncoder.encode(value));
        user.setEnabled(true);
        userRepository.save(user);
        var verificationTokenOptional = verificationTokenRepository.findByUser(user);
        verificationTokenOptional.ifPresent(verificationTokenRepository::delete);
        log.info("User {} has successfully reset their password", user.getUsername());
    }

    /**
     * Creates a new password reset token for the provided user.
     *
     * @param user the user to create the password reset token for
     * @return the created password reset token
     */
    @Override
    protected PasswordResetToken createToken(User user, EmailVerificationType emailVerificationType) {
        String token = UUID.randomUUID().toString();
        return passwordResetTokenRepository.save(PasswordResetToken.builder().user(user).token(token).build());
    }

    /**
     * Updates the provided password reset token by generating a new token value.
     *
     * @param token the password reset token to update
     * @return the updated password reset token
     */
    @Override
    protected PasswordResetToken updateToken(PasswordResetToken token) {
        String newToken = UUID.randomUUID().toString();
        token.setToken(newToken);
        return passwordResetTokenRepository.save(token);
    }

    /**
     * Checks whether the provided password reset token has expired.
     *
     * @param token the password reset token to check
     * @return true if the password reset token has expired, false otherwise
     */
    @Override
    protected boolean isTokenExpired(PasswordResetToken token) {
        LocalDateTime expiryDate = token.getExpiryDate();
        return expiryDate.isBefore(LocalDateTime.now());
    }

    /**
     * Sends a password reset email to the user with the provided email address.
     *
     * @param request the forgot password request object containing the user's email
     * @return the result message of the email sending operation
     */
    @Override
    public String sendPasswordResetEmail(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return sendTokenEmail(user, "Password Reset", "A password reset email", EmailVerificationType.PASSWORD_RESET);
    }

    /**
     * Resets the password for the user associated with the provided token.
     *
     * @param request     the reset password request object containing the new password
     * @param tokenString the password reset token
     * @return the result message of the password reset operation
     */
    @Override
    public String resetPassword(ResetPasswordRequest request, String tokenString) {
        var token = getTokenRepository().findByToken(tokenString).orElseThrow(() -> new InvalidRequestException("Invalid Token"));
        return verifyToken(token, request.getNewPassword());
    }
}

