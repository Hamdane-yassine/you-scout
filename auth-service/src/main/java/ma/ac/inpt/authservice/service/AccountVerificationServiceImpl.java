package ma.ac.inpt.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.model.VerificationToken;
import ma.ac.inpt.authservice.repository.TokenRepository;
import ma.ac.inpt.authservice.repository.UserRepository;
import ma.ac.inpt.authservice.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountVerificationServiceImpl extends AbstractTokenService<VerificationToken> implements AccountVerificationService {

    private final EmailService emailService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    @Override
    protected EmailService getEmailService() {
        return emailService;
    }

    @Override
    protected TokenRepository<VerificationToken> getTokenRepository() {
        return verificationTokenRepository;
    }

    @Override
    protected User getUserFromToken(VerificationToken token) {
        return token.getUser();
    }

    @Override
    protected String getTokenContent(VerificationToken token) {
        return "http://localhost:8082/api/v1/auth/confirm-account?token=" + token.getToken();
    }

    @Override
    protected void handleValidToken(User user,String value) {
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    protected VerificationToken createToken(User user) {
        String tokenString = UUID.randomUUID().toString();
        return verificationTokenRepository.save(VerificationToken.builder().user(user).token(tokenString).build());
    }

    @Override
    protected VerificationToken updateToken(VerificationToken token) {
        String tokenString = UUID.randomUUID().toString();
        token.setToken(tokenString);
        return verificationTokenRepository.save(token);
    }

    @Override
    protected boolean isTokenExpired(VerificationToken token) {
        LocalDateTime expiryDate = token.getExpiryDate();
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(expiryDate);
    }

    @Override
    public String sendVerificationEmail(User user) {
        return sendTokenEmail(user, "Verify your account", "A verification email");
    }

    @Override
    public String verifyAccount(String token) {
        return verifyToken(token,null);
    }
}


