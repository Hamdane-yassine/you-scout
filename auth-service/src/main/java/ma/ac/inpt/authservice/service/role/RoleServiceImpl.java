package ma.ac.inpt.authservice.service.role;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.exception.role.RoleAlreadyExistException;
import ma.ac.inpt.authservice.exception.role.RoleNotFoundException;
import ma.ac.inpt.authservice.dto.UserRoleRequest;
import ma.ac.inpt.authservice.repository.RoleRepository;
import ma.ac.inpt.authservice.repository.UserRepository;
import ma.ac.inpt.authservice.model.Role;
import ma.ac.inpt.authservice.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for managing roles in the application.
 */
@Service
@RequiredArgsConstructor
@Data
@Slf4j
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository; // Repository for accessing roles
    private final UserRepository userRepository; // Repository for accessing users

    @Value("#{'${default.app.roles}'.split(',')}")
    private List<String> defaultAppRoles; // List of default application roles

    @Value("#{'${default.user.roles}'.split(',')}")
    private List<String> defaultUserRoles; // List of default user roles

    /**
     * Save a new role in the system.
     *
     * @param role the role to be saved
     * @return the saved role
     */
    @Override
    public Role saveRole(Role role) {
        Optional<Role> optRole = roleRepository.findByRoleNameIgnoreCase(role.getRoleName());
        optRole.ifPresent(existingRole -> {
            throw new RoleAlreadyExistException("Role with name " + role.getRoleName() + " already exists.");
        });
        log.info("Saving new role with name: {}", role.getRoleName());
        return roleRepository.save(role);
    }

    /**
     * Assign a role to a user.
     *
     * @param request the UserRoleRequest containing username and role name
     * @return a message indicating the success of the operation
     */
    @Override
    public String assignRoleToUser(UserRoleRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Role role = roleRepository.findByRoleNameIgnoreCase(request.getRoleName())
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
        log.info("Role {} added to user {}", request.getRoleName(), request.getUsername());
        return "Role " + request.getRoleName() + " added to user " + request.getUsername();
    }

    /**
     * Assign default roles to a user when they register in the system.
     *
     * @param user the user to assign default roles to
     */
    @Override
    public void assignDefaultRolesToUser(User user) {
        Set<Role> existingDefaultRoles = new HashSet<>(roleRepository.findByRoleNameIn(defaultUserRoles));
        user.setRoles(existingDefaultRoles);
    }

    /**
     * Create default application roles during application startup.
     */
    @PostConstruct
    public void createDefaultAppRoles() {
        Set<String> existingRoleNames = roleRepository.findByRoleNameIn(defaultAppRoles)
                .stream()
                .map(Role::getRoleName)
                .collect(Collectors.toSet());

        List<Role> newRoles = defaultAppRoles.stream()
                .filter(roleName -> !existingRoleNames.contains(roleName))
                .map(roleName -> Role.builder().roleName(roleName).build())
                .collect(Collectors.toList());

        roleRepository.saveAll(newRoles);
        log.info("Default application roles created");
    }

    /**
     * Get a role by its ID.
     *
     * @param id the ID of the role
     * @return an Optional<Role> containing the role if it exists
     */
    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role Not found"));
    }

    /**
     * Update a role in the system.
     *
     * @param id   the ID of the role to be updated
     * @param role the updated role information
     * @return the updated role
     */
    @Override
    public Role updateRole(Long id, Role role) {
        Role updatedRole = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role Not found"));

        updatedRole.setRoleName(role.getRoleName());
        log.info("Updating role with id: {}", id);
        return roleRepository.save(updatedRole);
    }

    /**
     * Delete a role from the system by its ID.
     *
     * @param id the ID of the role to be deleted
     */
    @Override
    public void deleteRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role Not found"));

        roleRepository.delete(role);
        log.info("Role with id {} deleted", id);
    }

    /**
     * Remove a role from a user in the system.
     *
     * @param request the UserRoleRequest containing username and role name
     * @return a message indicating the success of the operation
     */
    @Override
    public String removeRoleFromUser(UserRoleRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Role role = roleRepository.findByRoleNameIgnoreCase(request.getRoleName())
                .orElseThrow(() -> new RoleNotFoundException("Role not found"));

        if (!user.getRoles().contains(role)) {
            return String.format("User %s does not have role %s", request.getUsername(), request.getRoleName());
        }

        user.getRoles().remove(role);
        userRepository.save(user);
        log.info("Role {} removed from user {}", request.getRoleName(), request.getUsername());
        return String.format("Role %s has been removed from user %s", request.getRoleName(), request.getUsername());
    }

    /**
     * Get all roles in the system.
     *
     * @return a Page<Role> containing the list of all roles
     */
    @Override
    public Page<Role> getAllRoles(Integer page, Integer size) {
        return roleRepository.findAll(PageRequest.of(page, size));
    }
}


