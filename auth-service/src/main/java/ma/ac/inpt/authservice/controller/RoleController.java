package ma.ac.inpt.authservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.model.Role;
import ma.ac.inpt.authservice.payload.UserRoleRequest;
import ma.ac.inpt.authservice.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
@RequestMapping("/api/v1/users/roles")
@Slf4j
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * Adds a new role.
     *
     * @param role the role to add
     * @return a ResponseEntity containing the added role
     */
    @PostMapping
    public ResponseEntity<Role> addRole(@RequestBody Role role) {
        log.info("Received request to add a new role: {}", role);
        Role addedRole = roleService.saveRole(role);
        log.info("Added new role: {}", addedRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedRole);
    }

    /**
     * Assigns a role to a user.
     *
     * @param request the UserRoleRequest containing the user ID and role ID
     * @return a ResponseEntity containing a success message
     */
    @PostMapping("/assign")
    public ResponseEntity<String> assignRoleToUser(@RequestBody UserRoleRequest request) {
        log.info("Received request to assign role to user: {}", request);
        String message = roleService.assignRoleToUser(request);
        log.info("Assigned role to user. Response: {}", message);
        return ResponseEntity.ok(message);
    }

    /**
     * Deletes a role by ID and removes the role from all users who have this role.
     *
     * @param id the ID of the role to delete
     * @return a ResponseEntity indicating success or failure
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        log.info("Received request to delete role with ID: {}", id);
        roleService.deleteRoleById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Retrieves all roles.
     *
     * @return a ResponseEntity containing a list of all roles
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
     * Retrieves a role by ID.
     *
     * @param id the ID of the role to retrieve
     * @return a ResponseEntity containing the requested role
     */
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        log.info("Received request to retrieve role with ID: {}", id);
        Optional<Role> role = roleService.getRoleById(id);
        return role.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Updates a role by ID.
     *
     * @param id   the ID of the role to update
     * @param role the updated role information
     * @return a ResponseEntity containing the updated role
     */
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role role) {
        log.info("Received request to update role with ID: {}", id);
        Optional<Role> existingRole = roleService.getRoleById(id);
        if (existingRole.isPresent()) {
            Role updatedRole = roleService.updateRole(id, role);
            return ResponseEntity.ok(updatedRole);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Removes a role from a user.
     *
     * @param request the UserRoleRequest containing the user ID and role ID
     * @return a ResponseEntity containing a success message
     */
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeRoleFromUser(@RequestBody UserRoleRequest request) {
        log.info("Received request to remove role from user: {}", request);
        String message = roleService.removeRoleFromUser(request);
        log.info("Removed role from user. Response: {}", message);
        return ResponseEntity.ok(message);
    }


}

