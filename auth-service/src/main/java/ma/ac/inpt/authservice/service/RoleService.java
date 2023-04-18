package ma.ac.inpt.authservice.service;


import ma.ac.inpt.authservice.model.Role;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.payload.UserRoleRequest;

import java.util.List;
import java.util.Optional;


/**
 * Defines methods to manage user roles.
 */
public interface RoleService {

    /**
     * Saves a new role to the database.
     *
     * @param role the role to be added
     * @return the saved role
     */
    Role saveRole(Role role);

    /**
     * Assigns a role to a user.
     *
     * @param request the request containing the user and role information
     * @return a message indicating the success or failure of the operation
     */
    String assignRoleToUser(UserRoleRequest request);

    /**
     * Adds default roles to a new user.
     *
     * @param user the user to add default roles to
     */
    void addDefaultRolesToUser(User user);

    /**
     * Adds default roles to the application.
     */
    void addDefaultAppRoles();

    /**
     * Gets a role by its ID.
     *
     * @param id the ID of the role to get
     * @return an Optional containing the role with the specified ID, or an empty Optional if not found
     */
    Optional<Role> getRoleById(Long id);

    /**
     * Updates a role with the specified ID.
     *
     * @param id   the ID of the role to update
     * @param role the updated role
     * @return the updated role
     */
    Role updateRole(Long id, Role role);

    /**
     * Deletes a role with the specified ID and removes the role from all users who have this role.
     *
     * @param id the ID of the role to delete
     */
    void deleteRoleById(Long id);

    /**
     * Removes a role from a user.
     *
     * @param request the UserRoleRequest containing the user ID and role ID
     * @return a message indicating the success or failure of the operation
     */
    String removeRoleFromUser(UserRoleRequest request);

    /**
     * Gets all roles in the database.
     *
     * @return a list of all roles in the database
     */
    List<Role> getAllRoles();
}


