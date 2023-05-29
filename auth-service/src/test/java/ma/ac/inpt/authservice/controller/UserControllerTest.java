package ma.ac.inpt.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ac.inpt.authservice.config.SecurityTestConfig;
import ma.ac.inpt.authservice.dto.*;
import ma.ac.inpt.authservice.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@Import(SecurityTestConfig.class)
class UserControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp() {
        // Arrange: Initialize the MockMvc object with the web application context
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @DisplayName("Test retrieving all users")
    @Test
    void testGetAllUsers() throws Exception {
        // Arrange: Define the UserServiceImpl response when getAllUsers method is called
        Mockito.when(userServiceImpl.getAllUsers(0, 20)).thenReturn(Page.empty());

        // Act & Assert: Execute the request and verify the HTTP status code
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Test deleting user by username")
    @Test
    void testDeleteUserByUsername() throws Exception {
        String username = "username";

        // Arrange: Define the UserServiceImpl behavior when deleteUserByUsername method is called
        Mockito.doNothing().when(userServiceImpl).deleteUserByUsername(username);

        // Act & Assert: Execute the request and verify the HTTP status code
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/users/" + username))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @DisplayName("Test getting user profile by username")
    @Test
    void testGetProfileByUsername() throws Exception {
        String username = "username";
        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .username(username)
                .email("testemail@domain.com")
                .build();

        // Arrange: Define the UserServiceImpl response when getUserDetailsByUsername method is called
        Mockito.when(userServiceImpl.getUserDetailsByUsername(username)).thenReturn(userDetailsDto);

        // Act & Assert: Execute the request and verify the HTTP status code
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + username + "/profile"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Test disabling user by username")
    @Test
    void testDisableUserByUsername() throws Exception {
        String username = "username";

        // Arrange: Define the UserServiceImpl behavior when updateUserEnabledStatus method is called with 'false'
        Mockito.doNothing().when(userServiceImpl).updateUserEnabledStatus(username, false);

        // Act & Assert: Execute the request and verify the HTTP status code
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/" + username + "/disable"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @DisplayName("Test enabling user by username")
    @Test
    void testEnableUserByUsername() throws Exception {
        String username = "username";

        // Arrange: Define the UserServiceImpl behavior when updateUserEnabledStatus method is called with 'true'
        Mockito.doNothing().when(userServiceImpl).updateUserEnabledStatus(username, true);

        // Act & Assert: Execute the request and verify the HTTP status code
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/" + username + "/enable"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @DisplayName("Test getting current user profile")
    @WithMockUser(username = "username")
    @Test
    void testGetCurrentUserProfile() throws Exception {
        String username = "username";
        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .username(username)
                .email("testemail@domain.com")
                .build();

        // Arrange: Define the UserServiceImpl response when getUserDetailsByUsername method is called
        Mockito.when(userServiceImpl.getUserDetailsByUsername(username)).thenReturn(userDetailsDto);

        // Act & Assert: Execute the request and verify the HTTP status code
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/me/profile"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Test updating current user profile")
    @WithMockUser(username = "username")
    @Test
    void testUpdateCurrentUserProfile() throws Exception {
        String username = "username";
        ProfileUpdateRequest request = new ProfileUpdateRequest();
        request.setFullName("test");

        ProfileUpdateResponse response = ProfileUpdateResponse.builder()
                .fullName("NewTest")
                .build();

        // Arrange: Define the UserServiceImpl response when updateProfileByUsername method is called
        Mockito.when(userServiceImpl.updateProfileByUsername(username, request)).thenReturn(response);

        // Act & Assert: Execute the request and verify the HTTP status code
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/me/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Test updating current user details")
    @WithMockUser(username = "username")
    @Test
    void testUpdateCurrentUserDetails() throws Exception {
        String username = "username";
        UserUpdateRequest request = new UserUpdateRequest();
        request.setEmail("newtestemail@domain.com");
        request.setUsername("newTestUsername");
        request.setPassword("OldPassword1!");
        request.setNewPassword("NewPassword1!");

        UserUpdateResponse response = UserUpdateResponse.builder().build();

        // Arrange: Define the UserServiceImpl response when updateUserByUsername method is called
        Mockito.when(userServiceImpl.updateUserByUsername(username, request)).thenReturn(response);

        // Act & Assert: Execute the request and verify the HTTP status code
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Test updating profile picture")
    @WithMockUser(username = "username")
    @Test
    void testUpdateProfilePicture() throws Exception {
        String username = "username";
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());
        ProfilePictureUpdateResponse response = ProfilePictureUpdateResponse.builder()
                .username(username)
                .profilePictureUrl("https://test.com/newtestpicture.jpg")
                .build();

        // Arrange: Define the UserServiceImpl response when updateProfilePicture method is called
        Mockito.when(userServiceImpl.updateProfilePicture(username, file)).thenReturn(response);

        // Act & Assert: Execute the request and verify the HTTP status code
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/users/me/profile/picture")
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

