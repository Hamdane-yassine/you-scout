package ma.ac.inpt.authservice.repository;

import ma.ac.inpt.authservice.dto.EmailVerificationType;
import ma.ac.inpt.authservice.model.PasswordResetToken;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.model.VerificationToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Token Repository Test")
class TokenRepositoryTest {
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User testUser;

    private VerificationToken verificationToken;
    private PasswordResetToken passwordResetToken;

    @BeforeEach
    @DisplayName("Setup for each test")
    void setup() {
        // Create a user for testing
        testUser = User.builder().username("testUser").email("test@example.com").isEnabled(true).build();

        // Persist the user to the database
        entityManager.persist(testUser);
        entityManager.flush();

        // Create tokens for testing
        verificationToken = VerificationToken.builder().token("verificationToken").user(testUser).emailVerificationType(EmailVerificationType.RESEND).build();
        passwordResetToken = PasswordResetToken.builder().token("passwordResetToken").user(testUser).build();

        // Persist the test tokens to the database
        verificationTokenRepository.save(verificationToken);
        passwordResetTokenRepository.save(passwordResetToken);
    }

    @AfterEach
    @DisplayName("Tear down after each test")
    void tearDown() {
        // Clean up the database after each test
        verificationTokenRepository.deleteAll();
        passwordResetTokenRepository.deleteAll();
    }
    @Test
    @DisplayName("Find Verification Token By Token Value Test")
    void findByTokenVerificationTokenRepositoryTest() {
        Optional<VerificationToken> foundToken = verificationTokenRepository.findByToken(verificationToken.getToken());
        assertTrue(foundToken.isPresent(), "Expected a valid token");
        assertEquals(verificationToken, foundToken.get(), "Expected VerificationToken should match the returned token");
    }

    @Test
    @DisplayName("Find Verification Token By User Test")
    void findByUserVerificationTokenRepositoryTest() {
        Optional<VerificationToken> foundToken = verificationTokenRepository.findByUser(testUser);
        assertTrue(foundToken.isPresent(), "Expected a valid token");
        assertEquals(verificationToken, foundToken.get(), "Expected VerificationToken should match the returned token");
    }

    @Test
    @DisplayName("Find Password Reset Token By Token Value Test")
    void findByTokenPasswordResetTokenRepositoryTest() {
        Optional<PasswordResetToken> foundToken = passwordResetTokenRepository.findByToken(passwordResetToken.getToken());
        assertTrue(foundToken.isPresent(), "Expected a valid token");
        assertEquals(passwordResetToken, foundToken.get(), "Expected PasswordResetToken should match the returned token");
    }

    @Test
    @DisplayName("Find Password Reset Token By User Test")
    void findByUserPasswordResetTokenRepositoryTest() {
        Optional<PasswordResetToken> foundToken = passwordResetTokenRepository.findByUser(testUser);
        assertTrue(foundToken.isPresent(), "Expected a valid token");
        assertEquals(passwordResetToken, foundToken.get(), "Expected PasswordResetToken should match the returned token");
    }
}