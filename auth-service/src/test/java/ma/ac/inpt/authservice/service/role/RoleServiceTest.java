package ma.ac.inpt.authservice.service.role;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ma.ac.inpt.authservice.dto.UserRoleRequest;
import ma.ac.inpt.authservice.exception.role.RoleAlreadyExistException;
import ma.ac.inpt.authservice.model.Role;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.repository.RoleRepository;
import ma.ac.inpt.authservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.*;

@DisplayName("Role Service Test")
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;
    private User user;
    private UserRoleRequest userRoleRequest;
    private final String roleName = "testRole";
    private final String username = "testUsername";

    @BeforeEach
    void setUp() {

        role = Role.builder().roleName(roleName).build();
        user = User.builder().username(username).password("password").email("test@gmail.com").roles(new HashSet<>()).build();
        userRoleRequest = new UserRoleRequest(username, roleName);
    }

    // Test for the saveRole() method
    @DisplayName("saveRole() - Given a new Role, should save the Role and return it")
    @Test
    void shouldSaveRole() {
        // given
        when(roleRepository.findByRoleNameIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // when
        Role savedRole = roleService.saveRole(role);

        // then
        assertEquals(role, savedRole);
    }

    // Test for the saveRole() method when an existing Role is provided
    @DisplayName("saveRole() - Given an existing Role, should throw RoleAlreadyExistException")
    @Test
    void shouldThrowRoleAlreadyExistExceptionWhenSavingExistingRole() {
        // given
        when(roleRepository.findByRoleNameIgnoreCase(anyString())).thenReturn(Optional.of(role));

        // when & then
        assertThrows(RoleAlreadyExistException.class, () -> roleService.saveRole(role));
    }

    // Test for the assignRoleToUser() method
    @DisplayName("assignRoleToUser() - Given a UserRoleRequest with valid username and role, should add the role to the user's roles and save the user")
    @Test
    void shouldAssignRoleToUser() {
        // given
        when(userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleNameIgnoreCase(anyString())).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        String message = roleService.assignRoleToUser(userRoleRequest);

        // then
        assertTrue(user.getRoles().contains(role));
        assertEquals("Role " + roleName + " added to user " + username, message);
    }

    // Test for the assignDefaultRolesToUser() method
    @DisplayName("assignDefaultRolesToUser() - Given a user, should assign default roles to the user")
    @Test
    void shouldAssignDefaultRolesToUser() {
        // given
        List<Role> defaultRoles = Collections.singletonList(Role.builder().roleName("USER").build());
        List<String> defaultUserRoles = Collections.singletonList("USER");
        when(roleRepository.findByRoleNameIgnoreCaseIn(defaultUserRoles)).thenReturn(defaultRoles);
        RoleServiceImpl roleService = new RoleServiceImpl(roleRepository, userRepository);
        roleService.setDefaultUserRoles(defaultUserRoles);

        // when
        roleService.assignDefaultRolesToUser(user);

        // then
        assertEquals(new HashSet<>(defaultRoles), user.getRoles());
    }


    // Test for the getRoleById() method
    @DisplayName("getRoleById() - Given a role ID, should return the role")
    @Test
    void shouldGetRoleById() {
        // given
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));

        // when
        Role foundRole = roleService.getRoleById(1L);

        // then
        assertEquals(role, foundRole);
    }

    // Test for the updateRole() method
    @DisplayName("updateRole() - Given a role ID and an updated Role, should return the updated role")
    @Test
    void shouldUpdateRole() {
        // given
        Role updatedRole = Role.builder().roleName("updatedRole").build();
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenReturn(updatedRole);

        // when
        Role savedRole = roleService.updateRole(1L, updatedRole);

        // then
        assertEquals(updatedRole, savedRole);
    }

    // Test for the deleteRoleById() method
    @DisplayName("deleteRoleById() - Given a role ID, should delete the role")
    @Test
    void shouldDeleteRoleById() {
        // given
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));

        // when & then
        assertDoesNotThrow(() -> roleService.deleteRoleById(1L));
    }

    // Test for the getAllRoles() method
    @DisplayName("getAllRoles() - should return a page of all roles")
    @Test
    void shouldGetAllRoles() {
        // given
        List<Role> roles = Collections.singletonList(role);
        Page<Role> pageRoles = new PageImpl<>(roles);

        when(roleRepository.findAll(any(PageRequest.class))).thenReturn(pageRoles);

        // when
        Page<Role> result = roleService.getAllRoles(0, 1);

        // then
        assertEquals(roles, result.getContent());
    }

    // Test for the removeRoleFromUser() method
    @DisplayName("removeRoleFromUser() - Given a UserRoleRequest with valid username and role, should remove the role from the user's roles")
    @Test
    void shouldRemoveRoleFromUser() {
        // given
        user.getRoles().add(role);

        when(userRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(roleRepository.findByRoleNameIgnoreCase(anyString())).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        String message = roleService.removeRoleFromUser(userRoleRequest);

        // then
        assertFalse(user.getRoles().contains(role));
        assertEquals("Role " + roleName + " has been removed from user " + username, message);
    }
}

