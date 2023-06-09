package ma.ac.inpt.authservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import ma.ac.inpt.authservice.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("User Repository Test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    @DisplayName("Setup for each test")
    void setup() {
        // Create a user for testing
        testUser = User.builder().username("testUser").email("test@example.com").isEnabled(true).build();

        // Persist the test user to the database
        userRepository.save(testUser);
    }

    @AfterEach
    @DisplayName("Tear down after each test")
    void tearDown() {
        // Clean up the database after each test
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Find by Username Test")
    void findByUsernameOrEmailWithUsernameTest() {
        // Test findByUsernameOrEmail method with username
        Optional<User> foundUser = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(testUser.getUsername());
        assertTrue(foundUser.isPresent(), "Expected a valid user when searching by username");
        assertEquals(testUser, foundUser.get(), "Expected User should match the returned user when searching by username");
    }

    @Test
    @DisplayName("Find by Email Test")
    void findByUsernameOrEmailWithEmailTest() {
        // Test findByUsernameOrEmail method with email
        Optional<User> foundUser = userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(testUser.getEmail());
        assertTrue(foundUser.isPresent(), "Expected a valid user when searching by email");
        assertEquals(testUser, foundUser.get(), "Expected User should match the returned user when searching by email");
    }

    @Test
    @DisplayName("Find by Username Test")
    void findByUsernameTest() {
        Optional<User> foundUser = userRepository.findByUsernameIgnoreCase(testUser.getUsername());
        assertTrue(foundUser.isPresent(), "Expected a valid user");
        assertEquals(testUser, foundUser.get(), "Expected User should match the returned user");
    }

    @Test
    @DisplayName("Find by Email Test")
    void findByEmailTest() {
        Optional<User> foundUser = userRepository.findByEmailIgnoreCase(testUser.getEmail());
        assertTrue(foundUser.isPresent(), "Expected a valid user");
        assertEquals(testUser, foundUser.get(), "Expected User should match the returned user");
    }

    @Test
    @DisplayName("Exists by Username Test")
    void existsByUsernameTest() {
        Boolean exists = userRepository.existsByUsernameIgnoreCase(testUser.getUsername());
        assertTrue(exists, "Expected user to exist");
    }

    @Test
    @DisplayName("Exists by Email Test")
    void existsByEmailTest() {
        Boolean exists = userRepository.existsByEmailIgnoreCase(testUser.getEmail());
        assertTrue(exists, "Expected user to exist");
    }
}


