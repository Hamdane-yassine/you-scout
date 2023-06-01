package ma.ac.inpt.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ac.inpt.authservice.config.SecurityTestConfig;
import ma.ac.inpt.authservice.dto.UserRoleRequest;
import ma.ac.inpt.authservice.model.Role;
import ma.ac.inpt.authservice.service.role.RoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@WebMvcTest(RoleController.class)
@Import(SecurityTestConfig.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @DisplayName("Test adding a new role")
    @Test
    void testAddRole() throws Exception {
        // Given
        Role role = Role.builder().roleName("SCOPE_ADMIN").build();
        Mockito.when(roleService.saveRole(role)).thenReturn(role);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(role)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @DisplayName("Test assigning a role to a user")
    @Test
    void testAssignRoleToUser() throws Exception {
        // Given
        UserRoleRequest request = UserRoleRequest.builder().username("testUser").roleName("admin").build();
        Mockito.when(roleService.assignRoleToUser(request)).thenReturn("Role assigned successfully");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/roles/assign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Test deleting a role")
    @Test
    void testDeleteRole() throws Exception {
        // Given
        Long roleId = 1L;
        Mockito.doNothing().when(roleService).deleteRoleById(roleId);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/roles/" + roleId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @DisplayName("Test retrieving all roles")
    @Test
    void testGetAllRoles() throws Exception {
        // Given
        Mockito.when(roleService.getAllRoles(0, 20)).thenReturn(Page.empty());

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/roles")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Test retrieving a role by ID")
    @Test
    void testGetRoleById() throws Exception {
        // Given
        Long roleId = 1L;
        Role role = Role.builder().id(roleId).roleName("SCOPE_ADMIN").build();
        Mockito.when(roleService.getRoleById(roleId)).thenReturn(role);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/roles/" + roleId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Test updating a role")
    @Test
    void testUpdateRole() throws Exception {
        // Given
        Long roleId = 1L;
        Role role = Role.builder().id(roleId).roleName("SCOPE_ADMIN").build();
        Mockito.when(roleService.updateRole(roleId, role)).thenReturn(role);

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/roles/" + roleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(role)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Test removing a role from a user")
    @Test
    void testRemoveRoleFromUser() throws Exception {
        // Given
        UserRoleRequest request = UserRoleRequest.builder().username("testUser").roleName("admin").build();
        Mockito.when(roleService.removeRoleFromUser(request)).thenReturn("Role removed successfully");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/roles/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}


