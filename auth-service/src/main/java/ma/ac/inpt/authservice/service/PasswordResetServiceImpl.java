package ma.ac.inpt.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.exception.UserNotFoundException;
import ma.ac.inpt.authservice.model.PasswordResetToken;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.payload.ForgotPasswordRequest;
import ma.ac.inpt.authservice.payload.ResetPasswordRequest;
import ma.ac.inpt.authservice.repository.PasswordResetTokenRepository;
import ma.ac.inpt.authservice.repository.TokenRepository;
import ma.ac.inpt.authservice.repository.UserRepository;
import ma.ac.inpt.authservice.repository.VerificationTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PasswordResetServiceImpl extends AbstractTokenService<PasswordResetToken> implements PasswordResetService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;


    @Override
    protected EmailService getEmailService() {
        return emailService;
    }

    @Override
    protected TokenRepository<PasswordResetToken> getTokenRepository() {
        return passwordResetTokenRepository;
    }

    @Override
    protected User getUserFromToken(PasswordResetToken token) {
        return token.getUser();
    }

    @Override
    protected String getTokenContent(PasswordResetToken token) {
        String tokenString = token.getToken();
        return "http://localhost:8082/api/v1/auth/reset-password?token=" + tokenString;
    }

    @Override
    protected void handleValidToken(User user, String value) {
        user.setPassword(passwordEncoder.encode(value));
        user.setEnabled(true);
        userRepository.save(user);
        var verificationTokenOptional = verificationTokenRepository.findByUser(user);
        verificationTokenOptional.ifPresent(verificationTokenRepository::delete);
    }

    @Override
    protected PasswordResetToken createToken(User user) {
        String token = UUID.randomUUID().toString();
        return passwordResetTokenRepository.save(PasswordResetToken.builder().user(user).token(token).build());
    }

    @Override
    protected PasswordResetToken updateToken(PasswordResetToken token) {
        String newToken = UUID.randomUUID().toString();
        token.setToken(newToken);
        return passwordResetTokenRepository.save(token);
    }

    @Override
    protected boolean isTokenExpired(PasswordResetToken token) {
        LocalDateTime expiryDate = token.getExpiryDate();
        return expiryDate.isBefore(LocalDateTime.now());
    }

    @Override
    public String sendPasswordResetEmail(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));
        return sendTokenEmail(user, "Password Reset Request", "A password reset email");
    }

    @Override
    public String resetPassword(ResetPasswordRequest request, String token) {
        return verifyToken(token, request.getNewPassword());
    }

}
