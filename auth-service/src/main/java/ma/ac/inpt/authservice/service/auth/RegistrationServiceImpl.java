package ma.ac.inpt.authservice.service.auth;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.dto.EmailVerificationType;
import ma.ac.inpt.authservice.exception.email.EmailAlreadyExistsException;
import ma.ac.inpt.authservice.exception.user.UsernameAlreadyExistsException;
import ma.ac.inpt.authservice.repository.UserRepository;
import ma.ac.inpt.authservice.exception.registration.InvalidRequestException;
import ma.ac.inpt.authservice.exception.registration.RegistrationException;
import ma.ac.inpt.authservice.model.Profile;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.dto.RegistrationRequest;
import ma.ac.inpt.authservice.service.role.RoleService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service implementation for handling registration functionality.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

    // Dependencies
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final EmailVerificationService emailVerificationService;

    /**
     * Registers a new user with the given registration request.
     *
     * @param request the registration request
     * @return a message indicating the result of the registration request
     * @throws InvalidRequestException if the registration request is invalid
     * @throws UsernameAlreadyExistsException if the username already exists
     * @throws EmailAlreadyExistsException if the email already exists
     * @throws RegistrationException if the registration failed due to an unknown error
     */
    @Override
    public String register(RegistrationRequest request) {
        String message;
        try {
            validateRegistrationRequest(request);
            // Save the user and send a verification email
            var user = saveUser(request, false);
            message = emailVerificationService.sendVerificationEmail(user, EmailVerificationType.REGISTRATION);
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Registration failed with error: {}", ex.getMessage());
            throw new RegistrationException("Registration failed with error: " + ex.getMessage());
        }
        log.info("Verification email has been sent to user: {}", request.getUsername());
        return message;
    }

    /**
     * Registers a new OAuth2 user with the given registration request.
     *
     * @param request the registration request
     */
    @Override
    public void registerOauth2User(RegistrationRequest request) {
        // If the user email does not already exist, save the user
        if (!userRepository.existsByEmail(request.getEmail())) {
            saveUser(request, true);
        }
    }

    /**
     * Validates the registration request by checking the username and email.
     *
     * @param request the registration request
     */
    private void validateRegistrationRequest(RegistrationRequest request) {
        validateUsername(request.getUsername());
        validateEmail(request.getEmail());
    }

    /**
     * Validates the username by checking if it already exists.
     *
     * @param username the username to validate
     */
    private void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(String.format("Username '%s' already exists", username));
        }
    }

    /**
     * Validates the email by checking if it already exists.
     *
     * @param email the email to validate
     */
    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(String.format("Email '%s' already exists", email));
        }
    }

    /**
     * Saves the user with the given registration request and sets its enabled status.
     *
     * @param request   the registration request
     * @param isEnabled the enabled status of the user
     * @return the saved user
     */
    private User saveUser(RegistrationRequest request, boolean isEnabled) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isEnabled(isEnabled)
                .profile(Profile.builder().fullName(request.getFullName()).build())
                .build();

        // Assign default roles to the user
        roleService.assignDefaultRolesToUser(user);
        // Save the user and return it
        return userRepository.save(user);
    }
}


