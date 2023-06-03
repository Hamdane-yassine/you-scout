package ma.ac.inpt.authservice.repository;

import ma.ac.inpt.authservice.model.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Role Repository Test")
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role testRole1, testRole2;

    @BeforeEach
    @DisplayName("Setup for each test")
    void setup() {
        // Create roles for testing
        testRole1 = Role.builder().roleName("SCOPE_ADMIN").build();
        testRole2 = Role.builder().roleName("SCOPE_USER").build();

        // Persist the test roles to the database for use in testing
        roleRepository.save(testRole1);
        roleRepository.save(testRole2);
    }

    @AfterEach
    @DisplayName("Tear down after each test")
    void tearDown() {
        // Remove any remaining data in the repository to maintain test isolation
        roleRepository.deleteAll();
    }

    @Test
    @DisplayName("Find by Role Name Ignore Case Test")
    void findByRoleNameIgnoreCaseTest() {
        // Check that we can find a role case insensitively
        Optional<Role> foundRole = roleRepository.findByRoleNameIgnoreCase(testRole1.getRoleName().toLowerCase());

        // Assert that the role was found as expected
        assertTrue(foundRole.isPresent(), "Expected a valid role");
        assertEquals(testRole1, foundRole.get(), "Expected Role should match the returned role");
    }

    @Test
    @DisplayName("Find by Role Name In Test")
    void findByRoleNameInTest() {
        // Check that we can find multiple roles by their names
        List<String> roleNames = Arrays.asList(testRole1.getRoleName(), testRole2.getRoleName());
        List<Role> foundRoles = roleRepository.findByRoleNameIn(roleNames);

        // Assert that the roles were found as expected
        assertNotNull(foundRoles, "Expected a valid list of roles");
        assertTrue(foundRoles.contains(testRole1), "Expected to find testRole1 in the returned list");
        assertTrue(foundRoles.contains(testRole2), "Expected to find testRole2 in the returned list");
    }
}


