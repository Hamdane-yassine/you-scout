package ma.ac.inpt.authservice.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.exception.email.EmailAlreadyExistsException;
import ma.ac.inpt.authservice.exception.user.UserNotFoundException;
import ma.ac.inpt.authservice.exception.user.UsernameAlreadyExistsException;
import ma.ac.inpt.authservice.messaging.UserEventMessagingService;
import ma.ac.inpt.authservice.payload.ProfileUpdateDto;
import ma.ac.inpt.authservice.payload.UserUpdateDto;
import ma.ac.inpt.authservice.repository.UserRepository;
import ma.ac.inpt.authservice.service.media.MediaService;
import ma.ac.inpt.authservice.model.Profile;
import ma.ac.inpt.authservice.model.Role;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.payload.UserDetailsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.stream.Collectors;

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
        return userPage.map(this::convertToUserDetailsDto);
    }

    /**
     * Delete a user by username.
     *
     * @param username the username of the user to delete
     */
    @Override
    public void deleteUserByUsername(String username) {
        log.info("Deleting user with username: {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
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
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
        return convertToUserDetailsDto(user);
    }

    /**
     * Update user information by username.
     *
     * @param username      the username of the user to update
     * @param userUpdateDto the UserUpdateDto containing the new information
     * @return the updated UserDetailsDto
     */
    @Override
    public UserDetailsDto updateUserByUsername(String username, UserUpdateDto userUpdateDto) {
        log.info("Updating user with username: {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
        //updateProfileFields(user, profileUpdateDto);
        userRepository.save(user);
        userEventMessagingService.sendUserUpdated(user);
        log.info("User with username '{}' has been updated.", username);
        return convertToUserDetailsDto(user);
    }

    /**
     * Update user information by username.
     *
     * @param username      the username of the user to update
     * @param profileUpdateDto the UserUpdateDto containing the new information
     * @return the updated UserDetailsDto
     */
    @Override
    public UserDetailsDto updateProfileByUsername(String username, ProfileUpdateDto profileUpdateDto) {
        log.info("Updating user profile with username: {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
        updateProfileFields(user, profileUpdateDto);
        userRepository.save(user);
        userEventMessagingService.sendUserUpdated(user);
        log.info("User with username '{}' has been updated.", username);
        return convertToUserDetailsDto(user);
    }

    /**
     * Update user's profile picture.
     *
     * @param username the username of the user to update the profile picture
     * @param file     the new profile picture file
     * @return the updated UserDetailsDto
     */
    @Override
    public UserDetailsDto updateProfilePicture(String username, MultipartFile file) {
        log.info("Updating profile picture for user with username: {}", username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));

        String oldProfilePictureUrl = user.getProfile().getProfilePicture();
        if (oldProfilePictureUrl != null) {
            mediaService.deleteFile(oldProfilePictureUrl);
        }

        String fileUrl = mediaService.uploadFile(file);
        user.getProfile().setProfilePicture(fileUrl);
        userRepository.save(user);

        log.info("Profile picture for user with username '{}' has been updated.", username);
        return convertToUserDetailsDto(user);
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
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));

        user.setEnabled(isEnabled);
        userRepository.save(user);
        log.info("Enabled status for user with username '{}' has been updated to {}.", username, isEnabled);
    }

    /**
     * Convert User entity to UserDetailsDto.
     *
     * @param user the User entity
     * @return the UserDetailsDto
     */
    private UserDetailsDto convertToUserDetailsDto(User user) {
        Profile profile = user.getProfile();
        return UserDetailsDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .fullName(profile.getFullName())
                .profilePicture(profile.getProfilePicture())
                .dateOfBirth(profile.getDateOfBirth())
                .gender(profile.getGender())
                .country(profile.getCountry())
                .cityOrRegion(profile.getCityOrRegion())
                .bio(profile.getBio())
                .socialMediaLinks(profile.getSocialMediaLinks())
                .isEnabled(user.isEnabled())
                .roles(user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()))
                .build();
    }

    /**
     * Update user's profile fields.
     *
     * @param user          the User entity
     * @param profileUpdateDto the UserUpdateDto containing the new information
     */
    private void updateProfileFields(User user, ProfileUpdateDto profileUpdateDto) {
        updateIfNotNull(profileUpdateDto.getFullName(), () -> user.getProfile().setFullName(profileUpdateDto.getFullName()));
        updateIfNotNull(profileUpdateDto.getDateOfBirth(), () -> user.getProfile().setDateOfBirth(profileUpdateDto.getDateOfBirth()));
        updateIfNotNull(profileUpdateDto.getGender(), () -> user.getProfile().setGender(profileUpdateDto.getGender()));
        updateIfNotNull(profileUpdateDto.getCountry(), () -> user.getProfile().setCountry(profileUpdateDto.getCountry()));
        updateIfNotNull(profileUpdateDto.getCityOrRegion(), () -> user.getProfile().setCityOrRegion(profileUpdateDto.getCityOrRegion()));
        updateIfNotNull(profileUpdateDto.getBio(), () -> user.getProfile().setBio(profileUpdateDto.getBio()));
        updateIfNotNull(profileUpdateDto.getSocialMediaLinks(), () -> user.getProfile().setSocialMediaLinks(profileUpdateDto.getSocialMediaLinks()));
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
        }
    }
}
