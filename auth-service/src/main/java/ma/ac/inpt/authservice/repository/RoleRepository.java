package ma.ac.inpt.authservice.repository;

import ma.ac.inpt.authservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Role entities.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Returns an optional Role object with the specified role name in case-insensitive manner.
     *
     * @param roleName the role name to search for
     * @return an Optional containing the Role object with the specified role name, or an empty Optional if not found
     */
    Optional<Role> findByRoleNameIgnoreCase(String roleName);

    /**
     * Returns a list of Role objects with role names in the specified list.
     *
     * @param roles the list of role names to search for
     * @return a List of Role objects with role names in the specified list
     */
    List<Role> findByRoleNameIn(List<String> roles);

}


