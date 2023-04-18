package ma.ac.inpt.authservice.repository;

import ma.ac.inpt.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * UserRepository is an interface for performing CRUD operations on the User entity.
 * It extends the JpaRepository interface, which provides all the basic CRUD methods.
 */

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return an Optional<User> object representing the user if found, otherwise an empty Optional
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user with a given username exists in the database.
     *
     * @param username the username to check for existence
     * @return a boolean value indicating whether a user with the given username exists
     */
    Boolean existsByUsername(String username);

    /**
     * Checks if a user with a given email exists in the database.
     *
     * @param email the email to check for existence
     * @return a boolean value indicating whether a user with the given email exists
     */
    Boolean existsByEmail(String email);

    /**
     * Removes the specified role from all users who have this role.
     *
     * @param roleId the ID of the role to remove
     * @return the number of users whose roles were updated
     */
    @Modifying
    @Query(value = "DELETE FROM users_roles where users_roles.roles_id = :roleId", nativeQuery = true)
    int removeRoleFromUsersByRoleId(@Param("roleId") Long roleId);

}