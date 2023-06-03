package ma.ac.inpt.authservice.service.auth;

import ma.ac.inpt.authservice.dto.ForgotPasswordRequest;
import ma.ac.inpt.authservice.dto.ResetPasswordRequest;

/**
 * Service interface for resetting passwords.
 */
public interface PasswordResetService {

    /**
     * Sends a password reset email to the user with the provided email address.
     *
     * @param request the forgot password request object containing the user's email
     * @return the result message of the email sending operation
     */
    String sendPasswordResetEmail(ForgotPasswordRequest request);

    /**
     * Resets the password for the user associated with the provided token.
     *
     * @param request     the reset password request object containing the new password
     * @param tokenString the password reset token
     * @return the result message of the password reset operation
     */
    String resetPassword(ResetPasswordRequest request, String tokenString);
}


