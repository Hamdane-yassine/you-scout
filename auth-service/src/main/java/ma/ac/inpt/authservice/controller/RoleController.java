package ma.ac.inpt.authservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.model.Role;
import ma.ac.inpt.authservice.payload.UserRoleRequest;
import ma.ac.inpt.authservice.service.role.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller class for managing roles.
 * Provides endpoints for CRUD operations on roles, and assigning/removing roles to/from users.
 */
@RestController
@RequestMapping("/api/v1/roles")
@PreAuthorize("hasAuthority('SCOPE_ADMIN')") // Requires the user to have 'SCOPE_ADMIN' authority
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    // Service class for managing roles
    private final RoleService roleService;

    /**
     * Endpoint for adding a new role.
     *
     * @param role The role to be added.
     * @return The added role.
     */
    @PostMapping
    public ResponseEntity<Role> addRole(@RequestBody Role role) {
        log.info("Received request to add a new role: {}", role);
        Role addedRole = roleService.saveRole(role);
        log.info("Added new role: {}", addedRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedRole);
    }

    /**
     * Endpoint for assigning a role to a user.
     *
     * @param request The UserRoleRequest object containing the role and user IDs.
     * @return A message indicating whether the operation was successful.
     */
    @PostMapping("/assign")
    public ResponseEntity<String> assignRoleToUser(@RequestBody UserRoleRequest request) {
        log.info("Received request to assign role to user: {}", request);
        String message = roleService.assignRoleToUser(request);
        log.info("Assigned role to user. Response: {}", message);
        return ResponseEntity.ok(message);
    }

    /**
     * Endpoint for deleting a role.
     *
     * @param id The ID of the role to be deleted.
     * @return A response entity with no content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        log.info("Received request to delete role with ID: {}", id);
        roleService.deleteRoleById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint for retrieving all roles.
     *
     * @return A list of all roles.
     */
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        log.info("Received request to retrieve all roles");
        List<Role> roles = roleService.getAllRoles();
        if (roles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(roles);
    }

    /**
     * Endpoint for retrieving a role by ID.
     *
     * @param id The ID of the role to be retrieved.
     * @return The role with the specified ID, or a not found response entity.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        log.info("Received request to retrieve role with ID: {}", id);
        Optional<Role> role = roleService.getRoleById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint for updating a role.
     *
     * @param id   The ID of the role to be updated.
     * @param role The updated role.
     * @return The updated role, or a not found response entity.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        log.info("Received request to update role with ID: {}", id);
        Optional<Role> existingRole = roleService.getRoleById(id);
        if (existingRole.isPresent()) {
            Role updatedRole = roleService.updateRole(id, role);
            return ResponseEntity.ok(updatedRole);
        }
        return ResponseEntity.ok(role);
    }

    /**
     * Endpoint for removing a role from a user.
     *
     * @param request The UserRoleRequest object containing the role and user IDs.
     * @return A message indicating whether the operation was successful.
     */
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeRoleFromUser(@RequestBody UserRoleRequest request) {
        log.info("Received request to remove role from user: {}", request);
        String message = roleService.removeRoleFromUser(request);
        log.info("Removed role from user. Response: {}", message);
        return ResponseEntity.ok(message);
    }
}


