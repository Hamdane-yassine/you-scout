package ma.ac.inpt.authservice.service;

import ma.ac.inpt.authservice.payload.ForgotPasswordRequest;
import ma.ac.inpt.authservice.payload.ResetPasswordRequest;

public interface PasswordResetService {

    String sendPasswordResetEmail(ForgotPasswordRequest request);

    String resetPassword(ResetPasswordRequest request, String token);
}
