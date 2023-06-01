package ma.ac.inpt.authservice.repository;

import ma.ac.inpt.authservice.model.RefreshToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Duration;
import java.time.Instant;


import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;


    private RefreshToken testToken;

    @BeforeEach
    void setup() {
        // Create a test refresh token
        testToken = RefreshToken.builder()
                .tokenUuid("testTokenUuid")
                .expiryDate(Instant.now().plus(Duration.ofDays(7)))
                .build();

        // Persist the refresh Token to the database
        refreshTokenRepository.save(testToken);

    }

    @AfterEach
    void tearDown() {
        // Clean the database
        refreshTokenRepository.deleteAll();
    }

    @Test
    void existsByTokenUuidTest() {
        // Check if the refresh token exists by its token UUID
        boolean exists = refreshTokenRepository.existsByTokenUuid(testToken.getTokenUuid());

        assertTrue(exists, "Expected refresh token to exist");
    }
}

