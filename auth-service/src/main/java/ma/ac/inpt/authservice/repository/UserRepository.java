package ma.ac.inpt.authservice.repository;

import ma.ac.inpt.authservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


/**
 * Repository interface for managing User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a list of users whose usernames contain the specified string.
     * This method supports case-sensitive searches.
     *
     * @param username The string to search for within the usernames. This value must not be null.
     * @return A list of User entities with usernames containing the specified string.
     *         If no users have usernames that contain the string, an empty list is returned.
     */
    Page<User> findByUsernameIgnoreCaseContaining(String username, Pageable pageable);

    /**
     * Returns an optional User object with the specified username or email.
     *
     * @param identifier the username or email to search for
     * @return an Optional containing the User object with the specified username or email, or an empty Optional if not found
     */
    @Query("SELECT u FROM User u WHERE lower(u.username) = lower(:identifier) OR lower(u.email) = lower(:identifier)")
    Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(@Param("identifier") String identifier);

    /**
     * Returns an optional User object with the specified username.
     *
     * @param username the username to search for
     * @return an Optional containing the User object with the specified username, or an empty Optional if not found
     */
    Optional<User> findByUsernameIgnoreCase(String username);

    /**
     * Returns an optional User object with the specified email.
     *
     * @param email the email to search for
     * @return an Optional containing the User object with the specified email, or an empty Optional if not found
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Returns true if a user with the specified username exists.
     *
     * @param username the username to search for
     * @return true if a user with the specified username exists, false otherwise
     */
    Boolean existsByUsernameIgnoreCase(String username);

    /**
     * Returns true if a user with the specified email exists.
     *
     * @param email the email to search for
     * @return true if a user with the specified email exists, false otherwise
     */
    Boolean existsByEmailIgnoreCase(String email);

}
