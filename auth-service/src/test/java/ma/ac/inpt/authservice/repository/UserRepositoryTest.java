package ma.ac.inpt.authservice.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import ma.ac.inpt.authservice.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setup() {
        // Create a user for testing
        testUser = User.builder().username("testUser").email("test@example.com").isEnabled(true).build();

        // Persist the test user to the database
        userRepository.save(testUser);
    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
        userRepository.deleteAll();
    }

    @Test
    void findByUsernameOrEmailWithUsernameTest() {
        // Test findByUsernameOrEmail method with username
        Optional<User> foundUser = userRepository.findByUsernameOrEmail(testUser.getUsername());
        assertTrue(foundUser.isPresent(), "Expected a valid user when searching by username");
        assertEquals(testUser, foundUser.get(), "Expected User should match the returned user when searching by username");
    }

    @Test
    void findByUsernameOrEmailWithEmailTest() {
        // Test findByUsernameOrEmail method with email
        Optional<User> foundUser = userRepository.findByUsernameOrEmail(testUser.getEmail());
        assertTrue(foundUser.isPresent(), "Expected a valid user when searching by email");
        assertEquals(testUser, foundUser.get(), "Expected User should match the returned user when searching by email");
    }

    @Test
    void findByUsernameTest() {
        Optional<User> foundUser = userRepository.findByUsername(testUser.getUsername());
        assertTrue(foundUser.isPresent(), "Expected a valid user");
        assertEquals(testUser, foundUser.get(), "Expected User should match the returned user");
    }

    @Test
    void findByEmailTest() {
        Optional<User> foundUser = userRepository.findByEmail(testUser.getEmail());
        assertTrue(foundUser.isPresent(), "Expected a valid user");
        assertEquals(testUser, foundUser.get(), "Expected User should match the returned user");
    }

    @Test
    void existsByUsernameTest() {
        Boolean exists = userRepository.existsByUsername(testUser.getUsername());
        assertTrue(exists, "Expected user to exist");
    }

    @Test
    void existsByEmailTest() {
        Boolean exists = userRepository.existsByEmail(testUser.getEmail());
        assertTrue(exists, "Expected user to exist");
    }

}


