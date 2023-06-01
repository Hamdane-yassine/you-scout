package ma.ac.inpt.authservice.repository;

import ma.ac.inpt.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


/**
 * Repository interface for managing User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Returns an optional User object with the specified username or email.
     *
     * @param identifier the username or email to search for
     * @return an Optional containing the User object with the specified username or email, or an empty Optional if not found
     */
    @Query("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier")
    Optional<User> findByUsernameOrEmail(@Param("identifier") String identifier);

    /**
     * Returns an optional User object with the specified username.
     *
     * @param username the username to search for
     * @return an Optional containing the User object with the specified username, or an empty Optional if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Returns an optional User object with the specified email.
     *
     * @param email the email to search for
     * @return an Optional containing the User object with the specified email, or an empty Optional if not found
     */
    Optional<User> findByEmail(String email);

    /**
     * Returns true if a user with the specified username exists.
     *
     * @param username the username to search for
     * @return true if a user with the specified username exists, false otherwise
     */
    Boolean existsByUsername(String username);

    /**
     * Returns true if a user with the specified email exists.
     *
     * @param email the email to search for
     * @return true if a user with the specified email exists, false otherwise
     */
    Boolean existsByEmail(String email);

}
