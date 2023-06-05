package ma.ac.inpt.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ac.inpt.authservice.config.SecurityTestConfig;
import ma.ac.inpt.authservice.dto.*;
import ma.ac.inpt.authservice.service.user.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(UserController.class)
@Import(SecurityTestConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userServiceImpl;

    @DisplayName("Test retrieving all users")
    @Test
    void testGetAllUsers() throws Exception {
        // Arrange: Define the UserServiceImpl response when getAllUsers method is called
        Mockito.when(userServiceImpl.getAllUsers(0, 20)).thenReturn(Page.empty());

        // Act & Assert: Execute the request and verify the HTTP status code
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
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
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/" + username))
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
        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + username + "/profile"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Test disabling user by username")
    @Test
    void testDisableUserByUsername() throws Exception {
        String username = "username";

        // Arrange: Define the UserServiceImpl behavior when updateUserEnabledStatus method is called with 'false'
        Mockito.doNothing().when(userServiceImpl).updateUserEnabledStatus(username, false);

        // Act & Assert: Execute the request and verify the HTTP status code
        mockMvc.perform(MockMvcRequestBuilders.put("/users/" + username + "/disable"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @DisplayName("Test enabling user by username")
    @Test
    void testEnableUserByUsername() throws Exception {
        String username = "username";

        // Arrange: Define the UserServiceImpl behavior when updateUserEnabledStatus method is called with 'true'
        Mockito.doNothing().when(userServiceImpl).updateUserEnabledStatus(username, true);

        // Act & Assert: Execute the request and verify the HTTP status code
        mockMvc.perform(MockMvcRequestBuilders.put("/users/" + username + "/enable"))
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
        mockMvc.perform(MockMvcRequestBuilders.get("/users/me/profile"))
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
        mockMvc.perform(MockMvcRequestBuilders.patch("/users/me/profile")
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
        request.setNewEmail("newtestemail@domain.com");
        request.setNewUsername("newTestUsername");
        request.setPassword("OldPassword1!");
        request.setNewPassword("NewPassword1!");

        UserUpdateResponse response = UserUpdateResponse.builder().build();

        // Arrange: Define the UserServiceImpl response when updateUserByUsername method is called
        Mockito.when(userServiceImpl.updateUserByUsername(username, request)).thenReturn(response);

        // Act & Assert: Execute the request and verify the HTTP status code
        mockMvc.perform(MockMvcRequestBuilders.patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Test updating profile picture")
    @WithMockUser(username = "username")
    @Test
    void testUpdateProfilePicture() throws Exception {
        String username = "username";
        MockMultipartFile file = new MockMultipartFile("file", "teddst.jpg", MediaType.IMAGE_JPEG_VALUE, "test image content".getBytes());
        ProfilePictureUpdateResponse response = ProfilePictureUpdateResponse.builder()
                .username(username)
                .profilePictureUrl("https://test.com/newtestpicture.jpg")
                .build();

        // Arrange: Define the UserServiceImpl response when updateProfilePicture method is called
        Mockito.when(userServiceImpl.updateProfilePicture(username, file)).thenReturn(response);

        // Act & Assert: Execute the request and verify the HTTP status code
        mockMvc.perform(MockMvcRequestBuilders.multipart("/users/me/profile/picture")
                        .file(file))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}


