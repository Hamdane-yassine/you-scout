package ma.ac.inpt.authservice.repository;

import ma.ac.inpt.authservice.model.RefreshToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Duration;
import java.time.Instant;


import static org.junit.jupiter.api.Assertions.assertTrue;

// Specifies that the class contains tests that should be run with the Spring Boot testing framework and
// focuses only on components that are typically used when testing a repository layer.
@DataJpaTest
@DisplayName("RefreshToken Repository Test")
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private RefreshToken testToken;

    @BeforeEach
    @DisplayName("Setup for each test")
    void setup() {
        // Create a test refresh token
        testToken = RefreshToken.builder()
                .tokenUuid("testTokenUuid")
                .expiryDate(Instant.now().plus(Duration.ofDays(7)))
                .build();

        // Persist the refresh Token to the database for use in testing
        refreshTokenRepository.save(testToken);
    }

    @AfterEach
    @DisplayName("Tear down after each test")
    void tearDown() {
        // Remove any remaining data in the repository to maintain test isolation
        refreshTokenRepository.deleteAll();
    }

    @Test
    @DisplayName("Exists by Token UUID Test")
    void existsByTokenUuidTest() {
        // Check if the refresh token exists by its token UUID
        boolean exists = refreshTokenRepository.existsByTokenUuid(testToken.getTokenUuid());

        // Assert that the refresh token was found as expected
        assertTrue(exists, "Expected refresh token to exist");
    }
}


