package ma.ac.inpt.authservice.repository;

import ma.ac.inpt.authservice.model.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role testRole1, testRole2;

    @BeforeEach
    void setup() {
        // Create roles for testing
        testRole1 = Role.builder().roleName("SCOPE_ADMIN").build();
        testRole2 = Role.builder().roleName("SCOPE_USER").build();

        // Persist the test roles to the database
        roleRepository.save(testRole1);
        roleRepository.save(testRole2);
    }

    @AfterEach
    void tearDown() {
        // Clean up the database after each test
        roleRepository.deleteAll();
    }

    @Test
    void findByRoleNameIgnoreCaseTest() {
        // Check that we can find a role case insensitively
        Optional<Role> foundRole = roleRepository.findByRoleNameIgnoreCase(testRole1.getRoleName().toLowerCase());

        assertTrue(foundRole.isPresent(), "Expected a valid role");
        assertEquals(testRole1, foundRole.get(), "Expected Role should match the returned role");
    }

    @Test
    void findByRoleNameInTest() {
        // Check that we can find multiple roles by their names
        List<String> roleNames = Arrays.asList(testRole1.getRoleName(), testRole2.getRoleName());
        List<Role> foundRoles = roleRepository.findByRoleNameIn(roleNames);

        assertNotNull(foundRoles, "Expected a valid list of roles");
        assertTrue(foundRoles.contains(testRole1), "Expected to find testRole1 in the returned list");
        assertTrue(foundRoles.contains(testRole2), "Expected to find testRole2 in the returned list");
    }
}

