package ma.ac.inpt.authservice.repository;

import ma.ac.inpt.authservice.model.RefreshToken;
import ma.ac.inpt.authservice.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TestEntityManager entityManager;


    private User testUser;
    private RefreshToken testToken;

    @BeforeEach
    void setup() {
        // Create a user for testing
        testUser = User.builder().username("testUser").email("test@example.com").isEnabled(true).build();

        // Persist the user to the database
        entityManager.persist(testUser);
        entityManager.flush();

        // Create a test refresh token
        testToken = RefreshToken.builder()
                .tokenUuid("testTokenUuid")
                .user(testUser)
                .expiryDate(Instant.now().plus(Duration.ofDays(7)))
                .build();

        // Persist the test user and token to the database
        refreshTokenRepository.save(testToken);
    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
        refreshTokenRepository.deleteAll();
    }

    @Test
    void deleteByUserTest() {
        // Delete the refresh token by the associated user
        refreshTokenRepository.deleteByUser(testUser);

        // Try to find the deleted refresh token by its ID
        Optional<RefreshToken> deletedToken = refreshTokenRepository.findById(testToken.getId());

        // Verify that the refresh token is deleted
        assertTrue(deletedToken.isEmpty(), "Expected refresh token to be deleted");
    }

    @Test
    void existsByTokenUuidTest() {
        // Check if the refresh token exists by its token UUID
        boolean exists = refreshTokenRepository.existsByTokenUuid(testToken.getTokenUuid());

        assertTrue(exists, "Expected refresh token to exist");
    }
}

