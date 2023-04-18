package ma.ac.inpt.authservice.repository;

import ma.ac.inpt.authservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * The RoleRepository interface provides CRUD operations for the Role entity
 * using Spring Data JPA.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a Role entity by its name in a case-insensitive manner.
     *
     * @param roleName the name of the role to find
     * @return an Optional containing the Role entity if found, otherwise empty
     */
    Optional<Role> findByRoleNameIgnoreCase(String roleName);

    /**
     * Finds all Role entities with names in the given list of roles.
     *
     * @param roles the list of role names to find
     * @return a List containing the matching Role entities
     */
    List<Role> findByRoleNameIn(List<String> roles);
}

