package ma.ac.inpt.authservice.service.role;


import ma.ac.inpt.authservice.model.Role;
import ma.ac.inpt.authservice.dto.UserRoleRequest;
import ma.ac.inpt.authservice.model.User;
import org.springframework.data.domain.Page;

import java.util.List;


/**
 * This interface defines methods for handling roles for users in the system.
 */
public interface RoleService {

    /**
     * Save a role in the system.
     *
     * @param role the role to save
     * @return the saved role
     */
    Role saveRole(Role role);

    /**
     * Assign a role to a user.
     *
     * @param request the request containing the user ID and role ID
     * @return a success message
     */
    String assignRoleToUser(UserRoleRequest request);

    /**
     * Assign default roles to a user when they register in the system.
     *
     * @param user the user to assign the roles to
     */
    void assignDefaultRolesToUser(User user);

    /**
     * Create default roles for the application.
     */
    void createDefaultAppRoles();

    /**
     * Get a role by ID.
     *
     * @param id the ID of the role to get
     * @return an optional role
     */
    Role getRoleById(Long id);

    /**
     * Update a role.
     *
     * @param id   the ID of the role to update
     * @param role the updated role
     * @return the updated role
     */
    Role updateRole(Long id, Role role);

    /**
     * Delete a role by ID.
     *
     * @param id the ID of the role to delete
     */
    void deleteRoleById(Long id);

    /**
     * Remove a role from a user.
     *
     * @param request the request containing the user ID and role ID
     * @return a success message
     */
    String removeRoleFromUser(UserRoleRequest request);

    /**
     * Get all roles in the system.
     *
     * @return a list of all roles
     */
    Page<Role> getAllRoles(Integer page, Integer size);
}


