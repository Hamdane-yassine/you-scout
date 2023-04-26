package ma.ac.inpt.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.exception.RoleAlreadyExistException;
import ma.ac.inpt.authservice.exception.RoleNotFoundException;
import ma.ac.inpt.authservice.model.Role;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.payload.UserRoleRequest;
import ma.ac.inpt.authservice.repository.RoleRepository;
import ma.ac.inpt.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    // Load default roles from application.properties
    @Value("#{'${default.app.roles}'.split(',')}")
    private List<String> defaultAppRoles;

    @Value("#{'${default.user.roles}'.split(',')}")
    private List<String> defaultUserRoles;

    /**
     * Adds a new role to the database.
     *
     * @param role The role to be added.
     * @return The added role.
     * @throws RoleAlreadyExistException If a role with the same name already exists.
     * @throws RoleNotFoundException     If the role is not found.
     */
    @Override
    public Role saveRole(Role role) {
        Optional<Role> optRole = roleRepository.findByRoleNameIgnoreCase(role.getRoleName());
        optRole.ifPresent(existingRole -> {
            throw new RoleAlreadyExistException("Role with name " + role.getRoleName() + " already exists.");
        });
        Role addedRole = roleRepository.save(role);
        log.info("Role {} added to the database.", addedRole.getRoleName());
        return addedRole;
    }

    /**
     * Assign a role to a user.
     *
     * @param request The UserRoleRequest containing the username and role name.
     * @return A message indicating that the role was added to the user.
     * @throws UsernameNotFoundException If the user is not found.
     * @throws RoleNotFoundException     If the role is not found.
     */
    @Override
    public String assignRoleToUser(UserRoleRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Role role = roleRepository.findByRoleNameIgnoreCase(request.getRoleName()).orElseThrow(() -> new RoleNotFoundException("Role not found"));
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
        log.info("Role {} assigned to user {}.", role.getRoleName(), user.getUsername());
        return "Role " + request.getRoleName() + " added to user " + request.getUsername();
    }

    /**
     * Adds default roles to a user.
     *
     * @param user The user to whom the default roles should be added.
     */
    @Override
    public void addDefaultRolesToUser(User user) {
        Set<Role> existingDefaultRoles = new HashSet<>(roleRepository.findByRoleNameIn(defaultUserRoles));
        user.setRoles(existingDefaultRoles);
        log.info(" default roles added to the user {}", user.getUsername());
    }

    /**
     * Adds default roles to the application.
     */
    @PostConstruct
    public void addDefaultAppRoles() {
        // Check if the default roles exist in the database
        Set<String> existingRoleNames = roleRepository.findByRoleNameIn(defaultAppRoles).stream().map(Role::getRoleName).collect(Collectors.toSet());

        // Insert missing default roles
        List<Role> newRoles = defaultAppRoles.stream().filter(roleName -> !existingRoleNames.contains(roleName)).map(roleName -> Role.builder().roleName(roleName).build()).collect(Collectors.toList());
        roleRepository.saveAll(newRoles);
        log.info("{} default roles added to the database.", newRoles.size());
    }

    /**
     * Fetches a role by its ID
     *
     * @param id the ID of the role to be fetched
     * @return an Optional containing the role if it exists, else an empty Optional
     * @throws RoleNotFoundException if the role is not found
     */
    @Override
    public Optional<Role> getRoleById(Long id) {
        log.info("Fetching Role by ID: {}", id);
        Optional<Role> role = Optional.ofNullable(roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException("Role Not found")));
        log.info("Role fetched: {}", role);
        return role;
    }

    /**
     * Updates a role by its ID
     *
     * @param id   the ID of the role to be updated
     * @param role the updated role object
     * @return the updated role object
     * @throws RoleNotFoundException if the role is not found
     */
    @Override
    public Role updateRole(Long id, Role role) {
        log.info("Updating Role with ID: {} and role object: {}", id, role);
        Role updatedRole = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException("Role Not found"));
        updatedRole.setRoleName(role.getRoleName());
        Role savedRole = roleRepository.save(updatedRole);
        log.info("Role updated: {}", savedRole);
        return savedRole;
    }

    /**
     * Deletes a role by its ID and removes the role from all users who have this role.
     *
     * @param id the ID of the role to be deleted
     * @throws RoleNotFoundException if the role is not found
     */
    @Override
    public void deleteRoleById(Long id) {
        log.info("Deleting Role with ID: {}", id);

        // Retrieve the role to delete
        Role role = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException("Role Not found"));

        // Remove the role from all users who have this role
        int numUsersUpdated = userRepository.removeRoleFromUsersByRoleId(id);

        // Delete the role
        roleRepository.delete(role);

        // Log the success message
        log.info("Role deleted successfully with ID: {}, and role removed from {} users.", id, numUsersUpdated);
    }


    /**
     * Fetches all roles from the database
     *
     * @return a list of all roles
     */
    @Override
    public List<Role> getAllRoles() {
        log.info("Fetching all roles");
        List<Role> roles = roleRepository.findAll();
        log.info("Total roles fetched: {}", roles.size());
        return roles;
    }

    /**
     * Removes a role from a user
     *
     * @param request a UserRoleRequest object containing the username and role name to be removed
     * @return a message indicating the success or failure of the operation
     * @throws UsernameNotFoundException if the user is not found
     * @throws RoleNotFoundException     if the role is not found
     */
    @Override
    public String removeRoleFromUser(UserRoleRequest request) {
        log.info("Removing role {} from user {}", request.getRoleName(), request.getUsername());
        User user = userRepository.findByUsernameOrEmail(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Role role = roleRepository.findByRoleNameIgnoreCase(request.getRoleName()).orElseThrow(() -> new RoleNotFoundException("Role not found"));

        if (!user.getRoles().contains(role)) {
            String message = String.format("User %s does not have role %s", request.getUsername(), request.getRoleName());
            log.info(message);
            return message;
        }

        user.getRoles().remove(role);
        userRepository.save(user);
        String message = String.format("Role %s has been removed from user %s", request.getRoleName(), request.getUsername());
        log.info(message);
        return message;
    }
}

