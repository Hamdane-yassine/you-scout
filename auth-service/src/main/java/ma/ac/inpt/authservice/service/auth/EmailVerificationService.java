package ma.ac.inpt.authservice.service.auth;

import ma.ac.inpt.authservice.dto.EmailVerificationType;
import ma.ac.inpt.authservice.model.User;

/**
 * Service interface for handling account verification functionality.
 */
public interface EmailVerificationService {

    /**
     * Sends an account verification email to the specified user.
     *
     * @param user the user to send the email to
     * @return a message indicating the status of the email sending process
     */
    String sendVerificationEmail(User user, EmailVerificationType emailVerificationType);

    /**
     * Verifies the specified account verification token and enables the associated user's account.
     *
     * @param token the account verification token to verify
     * @return a message indicating the status of the account verification process
     */
    String verifyAccount(String token);
}

