package ma.ac.inpt.authservice.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.dto.*;
import ma.ac.inpt.authservice.exception.email.EmailAlreadyExistsException;
import ma.ac.inpt.authservice.exception.user.PasswordInvalidException;
import ma.ac.inpt.authservice.exception.user.UsernameAlreadyExistsException;
import ma.ac.inpt.authservice.mapper.UserMapper;
import ma.ac.inpt.authservice.messaging.UserEventMessagingService;
import ma.ac.inpt.authservice.repository.UserRepository;
import ma.ac.inpt.authservice.service.auth.EmailVerificationService;
import ma.ac.inpt.authservice.service.auth.AuthenticationService;
import ma.ac.inpt.authservice.service.media.MediaService;
import ma.ac.inpt.authservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Service implementation for managing users.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MediaService mediaService;
    private final PasswordEncoder passwordEncoder;
    private final UserEventMessagingService userEventMessagingService;
    private final EmailVerificationService emailVerificationService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    /**
     * Get all users with pagination.
     *
     * @param page the page number
     * @param size the size of each page
     * @return a page of UserDetailsDto
     */
    @Override
    public Page<UserDetailsDto> getAllUsers(Integer page, Integer size) {
        log.info("Fetching all users with pagination - page: {}, size: {}", page, size);
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, size));
        return userPage.map(userMapper::userToUserDetailsDto);    }

    /**
     * Delete a user by username.
     *
     * @param username the username of the user to delete
     */
    @Override
    public void deleteUserByUsername(String username) {
        log.info("Deleting user with username: {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
        String ProfilePictureUrl = user.getProfile().getProfilePicture();
        if (ProfilePictureUrl != null) {
            mediaService.deleteFile(ProfilePictureUrl);
        }
        userRepository.delete(user);
        userEventMessagingService.sendUserDeleted(user);
        log.info("User with username '{}' has been deleted.", username);
    }

    /**
     * Get user details by username.
     *
     * @param username the username of the user
     * @return the UserDetailsDto containing user information
     */
        @Override
        public UserDetailsDto getUserDetailsByUsername(String username) {
            log.info("Fetching user details by username: {}", username);
            User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
            return userMapper.userToUserDetailsDto(user);
        }

    /**
     * Update user information by username.
     *
     * @param username      the username of the user to update
     * @param userUpdateRequest the UserUpdateDto containing the new information
     * @return the updated UserDetailsDto
     */
    @Override
    public UserUpdateResponse updateUserByUsername(String username, UserUpdateRequest userUpdateRequest) {
        log.info("Updating user with username: {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
        updateUserFields(user, userUpdateRequest);
        userRepository.save(user);
        if(userUpdateRequest.getUsername()!=null && userUpdateRequest.getEmail() == null)userEventMessagingService.sendUserUpdated(user);
        authenticationService.logout(username);
        log.info("User with username '{}' has been updated.", username);
        return userMapper.userToUserUpdateResponse(user);
    }

    /**
     * Update user profile information by username.
     *
     * @param username      the username of the user to update
     * @param profileUpdateRequest the UserUpdateDto containing the new information
     * @return the updated UserDetailsDto
     */
    @Override
    public ProfileUpdateResponse updateProfileByUsername(String username, ProfileUpdateRequest profileUpdateRequest) {
        log.info("Updating user profile with username: {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
        updateProfileFields(user, profileUpdateRequest);
        userRepository.save(user);
        userEventMessagingService.sendUserUpdated(user);
        log.info("User profile with username '{}' has been updated.", username);
        return userMapper.profileToProfileUpdateResponse(user.getProfile());
    }

    /**
     * Update user's profile picture.
     *
     * @param username the username of the user to update the profile picture
     * @param file     the new profile picture file
     * @return the updated UserDetailsDto
     */
    @Override
    public ProfilePictureUpdateResponse  updateProfilePicture(String username, MultipartFile file) {
        log.info("Updating profile picture for user with username: {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));

        String oldProfilePictureUrl = user.getProfile().getProfilePicture();
        if (oldProfilePictureUrl != null) {
            mediaService.deleteFile(oldProfilePictureUrl);
        }

        String fileUrl = mediaService.uploadFile(file);
        user.getProfile().setProfilePicture(fileUrl);
        userRepository.save(user);

        log.info("Profile picture for user with username '{}' has been updated.", username);
        return userMapper.userToProfilePictureUpdateResponse(user);
    }

    /**
     * Update user's enabled status.
     *
     * @param username  the username of the user to update the status
     * @param isEnabled the new enabled status
     */
    @Override
    public void updateUserEnabledStatus(String username, boolean isEnabled) {
        log.info("Updating enabled status for user with username: {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));

        user.setEnabled(isEnabled);
        userRepository.save(user);
        log.info("Enabled status for user with username '{}' has been updated to {}.", username, isEnabled);
    }

    /**
     * Update user's profile fields.
     *
     * @param user          the User entity
     * @param profileUpdateRequest the UserUpdateDto containing the new information
     */
    private void updateProfileFields(User user, ProfileUpdateRequest profileUpdateRequest) {
        updateIfNotNull(profileUpdateRequest.getFullName(), () -> user.getProfile().setFullName(profileUpdateRequest.getFullName()));
        updateIfNotNull(profileUpdateRequest.getDateOfBirth(), () -> user.getProfile().setDateOfBirth(profileUpdateRequest.getDateOfBirth()));
        updateIfNotNull(profileUpdateRequest.getGender(), () -> user.getProfile().setGender(profileUpdateRequest.getGender()));
        updateIfNotNull(profileUpdateRequest.getCountry(), () -> user.getProfile().setCountry(profileUpdateRequest.getCountry()));
        updateIfNotNull(profileUpdateRequest.getCityOrRegion(), () -> user.getProfile().setCityOrRegion(profileUpdateRequest.getCityOrRegion()));
        updateIfNotNull(profileUpdateRequest.getBio(), () -> user.getProfile().setBio(profileUpdateRequest.getBio()));
        updateIfNotNull(profileUpdateRequest.getSocialMediaLinks(), () -> user.getProfile().setSocialMediaLinks(profileUpdateRequest.getSocialMediaLinks()));
    }

    /**
     * Update user's fields.
     *
     * @param user          the User entity
     * @param userUpdateRequest the UserUpdateDto containing the new information
     */
    private void updateUserFields(User user, UserUpdateRequest userUpdateRequest) {
        checkUserPassword(user, userUpdateRequest.getPassword());
        updateIfNotNull(userUpdateRequest.getUsername(), () -> updateUsername(user, userUpdateRequest.getUsername()));
        updateIfNotNull(userUpdateRequest.getNewPassword(), () -> user.setPassword(passwordEncoder.encode(userUpdateRequest.getNewPassword())));
        updateIfNotNull(userUpdateRequest.getEmail(), () -> updateEmail(user, userUpdateRequest.getEmail()));
    }

    private void checkUserPassword(User user, String password) {
        if(!user.getPassword().equals(passwordEncoder.encode(password)))
            throw new PasswordInvalidException("Password invalid");
    }
    /**
     * Run the provided action if the value is not null.
     *
     * @param value  the value to check for null
     * @param action the action to run if the value is not null
     */
    private void updateIfNotNull(Object value, Runnable action) {
        Optional.ofNullable(value).ifPresent(v -> action.run());
    }

    /**
     * Update the user's username.
     *
     * @param user     the User entity
     * @param username the new username
     */
    private void updateUsername(User user, String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(String.format("Username '%s' already exists", username));
        } else {
            user.setUsername(username);
        }
    }

    /**
     * Update the user's email.
     *
     * @param user  the User entity
     * @param email the new email
     */
    private void updateEmail(User user, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(String.format("Email '%s' already exists", email));
        } else {
            user.setEmail(email);
            user.setEnabled(false);
            emailVerificationService.sendVerificationEmail(user,EmailVerificationType.UPDATING);
        }
    }
}
