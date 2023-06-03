package ma.ac.inpt.authservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ac.inpt.authservice.config.SecurityTestConfig;
import ma.ac.inpt.authservice.dto.*;
import ma.ac.inpt.authservice.service.auth.AuthenticationService;
import ma.ac.inpt.authservice.service.auth.EmailVerificationService;
import ma.ac.inpt.authservice.service.auth.PasswordResetService;
import ma.ac.inpt.authservice.service.auth.RegistrationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(AuthController.class)
@Import(SecurityTestConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private EmailVerificationService emailVerificationService;

    @MockBean
    private PasswordResetService passwordResetService;

    @DisplayName("Test user registration")
    @Test
    void testRegister() throws Exception {
        // Given
        RegistrationRequest request = RegistrationRequest.builder().
                username("testUser").
                fullName("Test User").
                email("test@mail.com").
                password("Password123!").
                build();
        String message = "User registered successfully";

        // When
        Mockito.when(registrationService.register(request)).thenReturn(message);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(message));
    }

    @DisplayName("Test user authentication")
    @Test
    void testAuthenticate() throws Exception {
        // Given
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username("testUser")
                .password("Password123!")
                .grantType("password")
                .build();
        AuthenticationResponse response = AuthenticationResponse.builder().
                accessToken("dummyAccessToken").
                refreshToken("dummyRefreshToken").
                build();

        // When
        Mockito.when(authenticationService.authenticate(request)).thenReturn(response);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Test account confirmation")
    @Test
    void testConfirmRegistration() throws Exception {
        // Given
        String token = "dummyToken";
        String message = "Account verified successfully";

        // When
        Mockito.when(emailVerificationService.verifyAccount(token)).thenReturn(message);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/confirm")
                        .param("token", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(message));
    }

    @DisplayName("Test forgot password request")
    @Test
    void testForgotPassword() throws Exception {
        // Given
        ForgotPasswordRequest request = ForgotPasswordRequest.builder().
                email("test@mail.com").
                build();
        String message = "Reset password email sent";

        // When
        Mockito.when(passwordResetService.sendPasswordResetEmail(request)).thenReturn(message);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/password/forgot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(message));
    }

    @DisplayName("Test reset password")
    @Test
    void testResetPassword() throws Exception {
        // Given
        ResetPasswordRequest request = ResetPasswordRequest.builder().
                email("test@mail.com").
                newPassword("NewPassword123!").
                build();
        String token = "dummyToken";
        String message = "Password reset successful";

        // When
        Mockito.when(passwordResetService.resetPassword(request, token)).thenReturn(message);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/password/reset")
                        .param("token", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(message));
    }

    @DisplayName("Test OAuth2 callback")
    @Test
    void testHandleGoogleCallback() throws Exception {
        // Given
        String code = "dummyCode";
        AuthenticationResponse response = AuthenticationResponse.builder().
                accessToken("dummyAccessToken").
                refreshToken("dummyRefreshToken").
                build();

        // When
        Mockito.when(authenticationService.authenticateOAuth2("google", code)).thenReturn(response);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/oauth2/callback")
                        .param("code", code))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Test user logout")
    @Test
    void testLogout() throws Exception {
        // Given
        String username = "testUser";

        // When
        Mockito.doNothing().when(authenticationService).logout(username);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/logout")
                        .param("username", username))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User logged out successfully."));
    }
}

