package ma.ac.inpt.authservice.service.auth;

import ma.ac.inpt.authservice.payload.ForgotPasswordRequest;
import ma.ac.inpt.authservice.payload.ResetPasswordRequest;

/**
 * Service interface for resetting passwords.
 */
public interface PasswordResetService {

    /**
     * Sends a password reset email to the user associated with the provided email address.
     *
     * @param request the request containing the user's email address
     * @return a message indicating the result of the email sending operation
     */
    String sendPasswordResetEmail(ForgotPasswordRequest request);

    /**
     * Resets the password of the user associated with the provided reset token.
     *
     * @param request the request containing the new password and the reset token
     * @param token   the reset token
     * @return a message indicating the result of the password reset operation
     */
    String resetPassword(ResetPasswordRequest request, String token);
}

