package ma.ac.inpt.authservice.service.auth;

import ma.ac.inpt.authservice.dto.RegistrationRequest;
import ma.ac.inpt.authservice.exception.email.EmailAlreadyExistsException;
import ma.ac.inpt.authservice.exception.registration.RegistrationException;
import ma.ac.inpt.authservice.exception.user.UsernameAlreadyExistsException;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.repository.UserRepository;
import ma.ac.inpt.authservice.service.role.RoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleService roleService;
    @Mock
    private EmailVerificationService emailVerificationService;

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @DisplayName("Test normal registration")
    @Test
    void shouldRegisterUser() {
        // Given
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("testUser");
        request.setEmail("testEmail");
        request.setFullName("Test User");
        request.setPassword("testPassword");

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(emailVerificationService.sendVerificationEmail(any(), any())).thenReturn("Email sent");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        String result = registrationService.register(request);

        // Then
        assertEquals("Email sent", result);
    }


    @DisplayName("Test registration with existing username")
    @Test
    void shouldThrowUsernameAlreadyExists() {
        // Given
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("existingUser");
        request.setEmail("testEmail");

        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // When / Then
        assertThrows(UsernameAlreadyExistsException.class, () -> registrationService.register(request));
    }

    @DisplayName("Test registration with existing email")
    @Test
    void shouldThrowEmailAlreadyExists() {
        // Given
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("testUser");
        request.setEmail("existingEmail");

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When / Then
        assertThrows(EmailAlreadyExistsException.class, () -> registrationService.register(request));
    }

    @DisplayName("Test registration with general error")
    @Test
    void shouldThrowRegistrationException() {
        // Given
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("testUser");
        request.setEmail("testEmail");

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenThrow(new RuntimeException("Test exception"));

        // When / Then
        assertThrows(RegistrationException.class, () -> registrationService.register(request));
    }

    @DisplayName("Test OAuth2 registration with new email")
    @Test
    void shouldRegisterOauth2User() {
        // Given
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("testUser");
        request.setEmail("newEmail");
        request.setPassword("testPassword");  // set the password

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        registrationService.registerOauth2User(request);

        // Then
        // Verify that saveUser is called
        verify(userRepository, times(1)).save(any(User.class));
    }


    @DisplayName("Test OAuth2 registration with existing email")
    @Test
    void shouldNotRegisterOauth2User() {
        // Given
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("testUser");
        request.setEmail("existingEmail");

        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When
        registrationService.registerOauth2User(request);

        // Then
        // Verify that saveUser is not called
        verify(userRepository, times(0)).save(any(User.class));
    }
}

