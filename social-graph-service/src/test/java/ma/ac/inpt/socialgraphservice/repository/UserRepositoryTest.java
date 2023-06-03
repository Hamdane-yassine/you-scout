package ma.ac.inpt.socialgraphservice.repository;


import ma.ac.inpt.socialgraphservice.model.User;
import org.junit.jupiter.api.*;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataNeo4jTest
@Transactional(propagation = Propagation.NEVER)
public class UserRepositoryTest {

    private static Neo4j embeddedDatabaseServer;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    static void initializeNeo4j() {
        // Set up an embedded Neo4j database
        embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .build();
    }

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        // Configure dynamic properties for connecting to the embedded Neo4j database
        registry.add("spring.neo4j.uri", embeddedDatabaseServer::boltURI);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", () -> null);
    }

    @AfterAll
    static void stopNeo4j() {
        // Close the embedded Neo4j database
        embeddedDatabaseServer.close();
    }

    @BeforeEach
    void setup() {
        // Clear the user repository before each test
        userRepository.deleteAll();
    }

    private User createUser(String username) {
        // Utility method to create a new user with the given username
        User user = new User();
        user.setUsername(username);
        return userRepository.save(user);
    }

    @Test
    @DisplayName("Test if a user exists by username")
    void testExistsByUsername() {
        // Given
        createUser("testUser");

        // When & Then
        assertTrue(userRepository.existsByUsername("testUser"));
    }

    @Test
    @DisplayName("Test following a user")
    void testFollowUser() {
        // Given
        User user1 = createUser("testUser1");
        User user2 = createUser("testUser2");

        // When
        userRepository.followUser(user1.getUsername(), user2.getUsername());

        // Then
        assertTrue(userRepository.isFollowing(user1.getUsername(), user2.getUsername()));
    }

    @Test
    @DisplayName("Test unfollowing a user")
    void testUnfollowUser() {
        // Given
        User user1 = createUser("testUser1");
        User user2 = createUser("testUser2");
        userRepository.followUser(user1.getUsername(), user2.getUsername());

        // When
        userRepository.unfollowUser(user1.getUsername(), user2.getUsername());

        // Then
        assertFalse(userRepository.isFollowing(user1.getUsername(), user2.getUsername()));
    }

    @Test
    @DisplayName("Test retrieving followers of a user")
    void testFindFollowers() {
        // Given
        User user1 = createUser("testUser1");
        User user2 = createUser("testUser2");
        userRepository.followUser(user1.getUsername(), user2.getUsername());

        // When
        Set<User> followers = userRepository.findFollowers(user2.getUsername());

        // Then
        assertNotNull(followers);
        assertEquals(1, followers.size());
        assertTrue(followers.contains(user1));
    }

    @Test
    @DisplayName("Test retrieving users that a user is following")
    void testFindFollowing() {
        // Given
        User user1 = createUser("testUser1");
        User user2 = createUser("testUser2");
        userRepository.followUser(user1.getUsername(), user2.getUsername());

        // When
        Set<User> following = userRepository.findFollowing(user1.getUsername());

        // Then
        assertNotNull(following);
        assertEquals(1, following.size());
        assertTrue(following.contains(user2));
    }

    @Test
    @DisplayName("Test blocking a user")
    void testBlockUser() {
        // Given
        User user1 = createUser("testUser1");
        User user2 = createUser("testUser2");

        // When
        userRepository.blockUser(user1.getUsername(), user2.getUsername());

        // Then
        assertTrue(userRepository.isBlocking(user1.getUsername(), user2.getUsername()));
    }

    @Test
    @DisplayName("Test retrieving users that a user has blocked")
    void testGetBlockedUsers() {
        // Given
        User user1 = createUser("testUser1");
        User user2 = createUser("testUser2");
        userRepository.blockUser(user1.getUsername(), user2.getUsername());

        // When
        Set<User> blockedUsers = userRepository.getBlockedUsers(user1.getUsername());

        // Then
        assertNotNull(blockedUsers);
        assertEquals(1, blockedUsers.size());
        assertTrue(blockedUsers.contains(user2));
    }

    @Test
    @DisplayName("Test unblocking a user")
    void testUnblockUser() {
        // Given
        User user1 = createUser("testUser1");
        User user2 = createUser("testUser2");
        userRepository.blockUser(user1.getUsername(), user2.getUsername());

        // When
        userRepository.unblockUser(user1.getUsername(), user2.getUsername());

        // Then
        assertFalse(userRepository.isBlocking(user1.getUsername(), user2.getUsername()));
    }

    @Test
    @DisplayName("Test count of followers")
    void testCountFollowers() {
        // Given
        User user1 = createUser("testUser1");
        User user2 = createUser("testUser2");
        userRepository.followUser(user1.getUsername(), user2.getUsername());

        // When & Then
        assertEquals(1, userRepository.countFollowers(user2.getUsername()));
    }

    @Test
    @DisplayName("Test count of following")
    void testCountFollowing() {
        // Given
        User user1 = createUser("testUser1");
        User user2 = createUser("testUser2");
        userRepository.followUser(user1.getUsername(), user2.getUsername());

        // When & Then
        assertEquals(1, userRepository.countFollowing(user1.getUsername()));
    }

    @Test
    @DisplayName("Test count of blocked users")
    void testCountBlockedUsers() {
        // Given
        User user1 = createUser("testUser1");
        User user2 = createUser("testUser2");
        userRepository.blockUser(user1.getUsername(), user2.getUsername());

        // When & Then
        assertEquals(1, userRepository.countBlockedUsers(user1.getUsername()));
    }
}
