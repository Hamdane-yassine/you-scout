package ma.ac.inpt.authservice.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.dto.EmailVerificationType;
import ma.ac.inpt.authservice.exception.registration.InvalidRequestException;
import ma.ac.inpt.authservice.messaging.UserEventMessagingService;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.model.VerificationToken;
import ma.ac.inpt.authservice.repository.TokenRepository;
import ma.ac.inpt.authservice.repository.UserRepository;
import ma.ac.inpt.authservice.repository.VerificationTokenRepository;
import ma.ac.inpt.authservice.service.email.EmailService;
import ma.ac.inpt.authservice.util.ApplicationBaseUrlRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Service implementation for handling account verification functionality.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmailVerificationServiceImpl extends AbstractTokenService<VerificationToken> implements EmailVerificationService {

    private final EmailService emailService; // Email service to send verification emails
    private final VerificationTokenRepository verificationTokenRepository; // Repository for verification tokens
    private final UserRepository userRepository; // Repository for users
    private final UserEventMessagingService userEventSender; // Service for sending user-related events

    private final ApplicationBaseUrlRetriever applicationBaseUrlRetriever; // Service for retrieving application base url

    /**
     * Returns the EmailService implementation to be used by this account verification service.
     *
     * @return the EmailService implementation to be used by this account verification service
     */
    @Override
    protected EmailService getEmailService() {
        return emailService;
    }

    /**
     * Returns the VerificationTokenRepository implementation to be used by this account verification service.
     *
     * @return the VerificationTokenRepository implementation to be used by this account verification service
     */
    @Override
    protected TokenRepository<VerificationToken> getTokenRepository() {
        return verificationTokenRepository;
    }

    /**
     * Returns the user associated with the specified verification token.
     *
     * @param token the verification token to get the user for
     * @return the user associated with the specified verification token
     */
    @Override
    protected User getUserFromToken(VerificationToken token) {
        return token.getUser();
    }

    /**
     * Returns the content of the specified verification token.
     *
     * @param token the verification token to get the content for
     * @return the content of the specified verification token
     */
    @Override
    protected String getTokenContent(VerificationToken token) {
        return applicationBaseUrlRetriever.getBaseUrl() + "/api/v1/auth/confirm?token=" + token.getToken();
    }

    /**
     * Handles a valid verification token by enabling the associated user's account and sending a UserCreated event.
     *
     * @param user  the user associated with the verified token
     * @param value not used in this implementation
     */
    @Override
    protected void handleValidToken(User user, String value) {
        user.setEnabled(true); // Enable the user's account
        userRepository.save(user); // Save the updated user to the database
        log.info("User {} has been successfully verified", user.getUsername());
    }

    /**
     * Creates a new verification token for the specified user.
     *
     * @param user the user to create the token for
     * @return the created verification token
     */
    @Override
    protected VerificationToken createToken(User user, EmailVerificationType emailVerificationType) {
        String tokenString = UUID.randomUUID().toString(); // Generate a random token string
        return verificationTokenRepository.save(VerificationToken.builder().user(user).token(tokenString).emailVerificationType(emailVerificationType).build());
    }

    /**
     * Updates the specified verification token with a new token string.
     *
     * @param token the verification token to update
     * @return the updated verification token
     */
    @Override
    protected VerificationToken updateToken(VerificationToken token) {
        String tokenString = UUID.randomUUID().toString(); // Generate a new random token string
        token.setToken(tokenString); // Set the token string of the token to the new value
        return verificationTokenRepository.save(token); // Save the updated verification token
    }

    /**
     * Returns whether the specified verification token has expired.
     *
     * @param token the verification token to check
     * @return true if the token has expired, false otherwise
     */
    @Override
    protected boolean isTokenExpired(VerificationToken token) {
        LocalDateTime expiryDate = token.getExpiryDate();
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(expiryDate);
    }

    /**
     * Sends a verification email to the specified user.
     *
     * @param user the user to send the verification email to
     * @return the result message of the email sending operation
     */
    @Override
    public String sendVerificationEmail(User user, EmailVerificationType emailVerificationType) {
        log.info("Sending verification email to user {}", user.getUsername());
        return sendTokenEmail(user, "Account verification", "A verification email", emailVerificationType);
    }

    /**
     * Verifies the account associated with the specified token.
     *
     * @param tokenString the token to verify the account for
     * @return the result message of the account verification operation
     */
    @Override
    public String verifyAccount(String tokenString) {
        log.info("Verifying account with token {}", tokenString);
        var token = getTokenRepository().findByToken(tokenString).orElseThrow(() -> new InvalidRequestException("Invalid Token"));
        String message = verifyToken(token, null);
        var user = getUserFromToken(token);
        switch (token.getEmailVerificationType()) {
            case REGISTRATION -> userEventSender.sendUserCreated(user);
            case UPDATING -> userEventSender.sendUserUpdated(user);
        }
        return message;
    }
}




