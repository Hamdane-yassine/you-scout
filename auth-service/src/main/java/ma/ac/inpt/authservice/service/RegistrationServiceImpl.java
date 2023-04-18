package ma.ac.inpt.authservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.exception.EmailAlreadyExistsException;
import ma.ac.inpt.authservice.exception.InvalidRequestException;
import ma.ac.inpt.authservice.exception.RegistrationException;
import ma.ac.inpt.authservice.exception.UsernameAlreadyExistsException;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.payload.AuthenticationRequest;
import ma.ac.inpt.authservice.payload.AuthenticationResponse;
import ma.ac.inpt.authservice.payload.RegisterRequest;
import ma.ac.inpt.authservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * Service class that handles user registration.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RegistrationServiceImpl implements RegistrationService {

    /**
     * UserRepository instance for user data access
     */
    private final UserRepository userRepository;

    /**
     * PasswordEncoder instance for encoding passwords
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Validator instance for validating request objects
     */
    private final Validator validator;

    /**
     * AuthenticationService instance for user authentication
     */
    private final AuthenticationService authenticationService;
    private final RoleService roleService;

    /**
     * Registers a new user with the provided registration details.
     *
     * @param request the registration request containing user details
     * @return an authentication response after the user has been registered
     * @throws InvalidRequestException if the registration request is invalid
     * @throws RegistrationException   if registration fails for any other reason
     */
    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            log.error("Invalid registration request: {}", violations);
            throw new InvalidRequestException("Invalid registration request");
        }

        try {
            validateRegistrationRequest(request);
            saveUser(request);
        } catch (UsernameAlreadyExistsException | EmailAlreadyExistsException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Registration failed with error: {}", ex.getMessage());
            throw new RegistrationException("Registration failed");
        }
        log.info("Successfully registered user with username {}", request.getUsername());
        return authenticationService.authenticate(AuthenticationRequest.builder().grantType("PASSWORD").withRefreshToken(request.isWithRefreshToken()).password(request.getPassword()).username(request.getUsername()).build());
    }

    /**
     * Validates the registration request by checking if the provided username and email are already taken.
     *
     * @param request the registration request containing user details
     * @throws UsernameAlreadyExistsException if the username is already taken
     * @throws EmailAlreadyExistsException    if the email is already taken
     */
    private void validateRegistrationRequest(RegisterRequest request) {
        validateUsername(request.getUsername());
        validateEmail(request.getEmail());
    }

    /**
     * Validates if the provided username is already taken.
     *
     * @param username the username to be validated
     * @throws UsernameAlreadyExistsException if the username is already taken
     */
    private void validateUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(String.format("Username '%s' already exists", username));
        }
    }

    /**
     * Validates if the provided email is already taken.
     *
     * @param email the email to be validated
     * @throws EmailAlreadyExistsException if the email is already taken
     */
    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(String.format("Email '%s' already exists", email));
        }
    }

    /**
     * Saves the new user to the repository after encoding the password.
     *
     * @param request the registration request containing user details
     */
    private void saveUser(RegisterRequest request) {
        var user = User.builder().username(request.getUsername()).email(request.getEmail()).password(passwordEncoder.encode(request.getPassword())).firstname(request.getFirstname()).lastname(request.getLastname()).build();
        roleService.addDefaultRolesToUser(user);
        userRepository.save(user);
    }

}
