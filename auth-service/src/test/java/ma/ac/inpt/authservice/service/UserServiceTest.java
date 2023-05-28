package ma.ac.inpt.authservice.service;

import ma.ac.inpt.authservice.exception.email.EmailAlreadyExistsException;
import ma.ac.inpt.authservice.exception.user.PasswordInvalidException;
import ma.ac.inpt.authservice.exception.user.UsernameAlreadyExistsException;
import ma.ac.inpt.authservice.model.Profile;
import ma.ac.inpt.authservice.service.user.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import ma.ac.inpt.authservice.dto.*;
import ma.ac.inpt.authservice.mapper.UserMapper;
import ma.ac.inpt.authservice.messaging.UserEventMessagingService;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.repository.UserRepository;
import ma.ac.inpt.authservice.service.auth.EmailVerificationService;
import ma.ac.inpt.authservice.service.auth.AuthenticationService;
import ma.ac.inpt.authservice.service.media.MediaService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    // Mock dependencies
    @Mock
    private UserRepository userRepository;
    @Mock
    private MediaService mediaService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserEventMessagingService userEventMessagingService;
    @Mock
    private EmailVerificationService emailVerificationService;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private UserMapper userMapper;

    // Class under test
    @InjectMocks
    private UserServiceImpl userService;

    // Test data
    private User user;
    private UserDetailsDto userDetailsDto;
    private ProfileUpdateRequest profileUpdateRequest;
    private UserUpdateRequest userUpdateRequest;
    private MultipartFile file;
    private final String username = "testUsername";

    @BeforeEach
    public void setUp() {
        // Create test objects
        user = User.builder().username(username).password("password").email("test@gmail.com").profile(Profile.builder().profilePicture("test.jpg").build()).build();
        userDetailsDto = UserDetailsDto.builder().build();
        profileUpdateRequest = new ProfileUpdateRequest();
        userUpdateRequest = new UserUpdateRequest();
        file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image".getBytes());
    }

    // Testing 'getAllUsers' method
    @Test
    public void shouldGetAllUsers() {
        // Given
        List<User> users = new ArrayList<>();
        users.add(user);
        Page<User> userPage = new PageImpl<>(users);
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);
        when(userMapper.userToUserDetailsDto(any())).thenReturn(userDetailsDto);

        // When
        Page<UserDetailsDto> returnedPage = userService.getAllUsers(0, 10);

        // Then
        verify(userRepository, times(1)).findAll(any(PageRequest.class));
        assertNotNull(returnedPage);
    }

    // Testing 'deleteUserByUsername' method when profile picture is not null
    @Test
    public void shouldDeleteUserWithProfilePictureByUsername() {
        // Given
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        userService.deleteUserByUsername(username);

        // Then
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).delete(user);
        verify(userEventMessagingService, times(1)).sendUserDeleted(user);
        verify(mediaService, times(1)).deleteFile(user.getProfile().getProfilePicture());
    }

    // Testing 'deleteUserByUsername' method when profile picture is null
    @Test
    public void shouldDeleteUserWithoutProfilePictureByUsername() {
        // Given
        user.getProfile().setProfilePicture(null);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        userService.deleteUserByUsername(username);

        // Then
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).delete(user);
        verify(userEventMessagingService, times(1)).sendUserDeleted(user);
        verify(mediaService, never()).deleteFile(any());
    }


    // Testing 'getUserDetailsByUsername' method
    @Test
    public void shouldGetUserDetailsByUsername() {
        // Given
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userMapper.userToUserDetailsDto(user)).thenReturn(userDetailsDto);

        // When
        UserDetailsDto returnedDto = userService.getUserDetailsByUsername(username);

        // Then
        verify(userRepository, times(1)).findByUsername(username);
        assertNotNull(returnedDto);
        assertEquals(userDetailsDto, returnedDto);
    }

    // Testing 'updateUserByUsername' method - Updating user with username and email
    @Test
    public void shouldUpdateUserByUsernameWithUsernameAndEmail() {
        // Given
        userUpdateRequest.setPassword("password");
        userUpdateRequest.setUsername("newUsername");
        userUpdateRequest.setNewPassword("newPassword");
        userUpdateRequest.setEmail("newtest@gmail.com");
        UserUpdateResponse expectedResponse = UserUpdateResponse.builder().username("newUsername").email("test@gmail.com").build();
        when(passwordEncoder.encode("password")).thenReturn("password");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userMapper.userToUserUpdateResponse(user)).thenReturn(expectedResponse);

        // When
        UserUpdateResponse response = userService.updateUserByUsername(username, userUpdateRequest);

        // Then
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(user);
        verify(userEventMessagingService, never()).sendUserUpdated(user);
        verify(authenticationService, times(1)).logout(username);
        verify(emailVerificationService,times(1)).sendVerificationEmail(user,EmailVerificationType.UPDATING);
        assertEquals(expectedResponse, response);
    }

    // Testing 'updateUserByUsername' method - Updating user with only the username
    @Test
    public void shouldUpdateUserByUsernameWithUsernameOnly() {
        // Given
        userUpdateRequest.setPassword("password");
        userUpdateRequest.setUsername("newUsername");
        userUpdateRequest.setNewPassword("newPassword");
        UserUpdateResponse expectedResponse = UserUpdateResponse.builder().username("newUsername").email("test@gmail.com").build();
        when(passwordEncoder.encode("password")).thenReturn("password");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userMapper.userToUserUpdateResponse(user)).thenReturn(expectedResponse);

        // When
        UserUpdateResponse response = userService.updateUserByUsername(username, userUpdateRequest);

        // Then
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(user);
        verify(userEventMessagingService,times(1)).sendUserUpdated(user);
        verify(authenticationService, times(1)).logout(username);
        verify(emailVerificationService,never()).sendVerificationEmail(any(),any());
        assertEquals(expectedResponse, response);
    }

    // Testing 'updateUserByUsername' method - Incorrect password
    @Test
    public void shouldThrowExceptionWhenPasswordIsIncorrect() {
        // Given
        String incorrectPassword = "incorrectPassword";
        userUpdateRequest.setPassword(incorrectPassword);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Then
        assertThrows(PasswordInvalidException.class, () -> userService.updateUserByUsername(username, userUpdateRequest));
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, never()).save(any());
        verify(userEventMessagingService, never()).sendUserUpdated(any());
        verify(authenticationService, never()).logout(any());
    }

    // Testing 'updateUserByUsername' method - Username already exists
    @Test
    public void shouldThrowExceptionWhenUsernameAlreadyExists() {
        // Given
        String existingUsername = "existingUser";
        userUpdateRequest.setPassword("password");
        userUpdateRequest.setUsername(existingUsername);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername(existingUsername)).thenReturn(true);
        when(passwordEncoder.encode("password")).thenReturn("password");

        // Then
        assertThrows(UsernameAlreadyExistsException.class, () -> userService.updateUserByUsername(username, userUpdateRequest));
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, never()).save(any());
        verify(userEventMessagingService, never()).sendUserUpdated(any());
        verify(userEventMessagingService, never()).sendUserUpdated(any());
        verify(authenticationService, never()).logout(any());
    }

    // Testing 'updateUserByUsername' method - Email already exists
    @Test
    public void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Given
        String existingEmail = "existingEmail";
        userUpdateRequest.setPassword("password");
        userUpdateRequest.setEmail(existingEmail);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmail(existingEmail)).thenReturn(true);
        when(passwordEncoder.encode("password")).thenReturn("password");

        // Then
        assertThrows(EmailAlreadyExistsException.class, () -> userService.updateUserByUsername(username, userUpdateRequest));
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, never()).save(any());
        verify(userEventMessagingService, never()).sendUserUpdated(any());
        verify(userEventMessagingService, never()).sendUserUpdated(any());
        verify(authenticationService, never()).logout(any());
    }

    // Testing 'updateProfileByUsername' method
    @Test
    public void shouldUpdateProfileByUsername() {
        // Given
        profileUpdateRequest.setFullName("test");
        ProfileUpdateResponse expectedResponse = ProfileUpdateResponse.builder().fullName("test").build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userMapper.profileToProfileUpdateResponse(user.getProfile())).thenReturn(expectedResponse);

        // When
        ProfileUpdateResponse response = userService.updateProfileByUsername(username, profileUpdateRequest);

        // Then
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(user);
        verify(userEventMessagingService,times(1)).sendUserUpdated(user);
        assertEquals(expectedResponse, response);

    }

    // Testing 'updateProfilePicture' method
    @Test
    public void shouldUpdateProfilePictureWithUserHasOldPicture() {
        // Given
        String fileUrl = "testUrl";
        ProfilePictureUpdateResponse expectedResponse = ProfilePictureUpdateResponse.builder().username(username).profilePictureUrl("testUrl").build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(mediaService.uploadFile(file)).thenReturn(fileUrl);
        when(userMapper.userToProfilePictureUpdateResponse(user)).thenReturn(expectedResponse);

        // When
        ProfilePictureUpdateResponse response = userService.updateProfilePicture(username, file);

        // Then
        verify(userRepository, times(1)).findByUsername(username);
        verify(mediaService, times(1)).uploadFile(file);
        verify(userRepository, times(1)).save(user);
        assertEquals(expectedResponse, response);
    }

    // Testing 'updateUserEnabledStatus' method
    @Test
    public void shouldUpdateUserEnabledStatus() {
        // Given
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        userService.updateUserEnabledStatus(username, true);

        // Then
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(user);
        assertTrue(user.isEnabled());
    }

    @Test
    public void shouldThrowExceptionWhenUserNotFound() {
        // Given
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Then
        assertThrows(UsernameNotFoundException.class, () -> userService.getUserDetailsByUsername(username));
        assertThrows(UsernameNotFoundException.class, () -> userService.deleteUserByUsername(username));
        assertThrows(UsernameNotFoundException.class, () -> userService.updateUserByUsername(username, userUpdateRequest));
        assertThrows(UsernameNotFoundException.class, () -> userService.updateProfileByUsername(username, profileUpdateRequest));
        assertThrows(UsernameNotFoundException.class, () -> userService.updateProfilePicture(username, file));
        assertThrows(UsernameNotFoundException.class, () -> userService.updateUserEnabledStatus(username, true));
    }
}
