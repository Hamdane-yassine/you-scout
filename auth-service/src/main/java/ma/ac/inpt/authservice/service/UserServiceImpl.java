package ma.ac.inpt.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.exception.EmailAlreadyExistsException;
import ma.ac.inpt.authservice.exception.UserNotFoundException;
import ma.ac.inpt.authservice.exception.UsernameAlreadyExistsException;
import ma.ac.inpt.authservice.messaging.UserEventSender;
import ma.ac.inpt.authservice.model.Profile;
import ma.ac.inpt.authservice.model.Role;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.payload.UserDetailsDto;
import ma.ac.inpt.authservice.payload.UserUpdateDto;
import ma.ac.inpt.authservice.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MediaService mediaService;
    private final PasswordEncoder passwordEncoder;

    private final UserEventSender userEventSender;

    /**
     * Retrieve all users
     *
     * @return Page of UserDetailsDto
     */
    @Override
    public Page<UserDetailsDto> getAllUsers(Integer page, Integer size) {
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, size));
        return userPage.map(this::convertToUserDetailsDto);
    }

    /**
     * Delete a user by their username
     *
     * @param username The username of the user
     * @throws UserNotFoundException If the user is not found
     */
    @Override
    public void deleteUserByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
        String ProfilePictureUrl = user.getProfile().getProfilePicture();
        if (ProfilePictureUrl != null) {
            mediaService.deleteFile(ProfilePictureUrl);
        }
        userRepository.delete(user);
        userEventSender.sendUserDeleted(user);
    }

    /**
     * Retrieve a user's profile by their username
     *
     * @param username The username of the user
     * @return UserDetailsDto
     * @throws UserNotFoundException If the user is not found
     */
    @Override
    public UserDetailsDto getUserDetailsByUsername(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
        return convertToUserDetailsDto(user);
    }

    /**
     * Partially update a user's profile by their username
     *
     * @param username      The username of the user
     * @param userUpdateDto The dto for request updating
     * @return UserDetailsDto
     * @throws UserNotFoundException If the user is not found
     */
    @Override
    public UserDetailsDto UpdateUserByUsername(String username, UserUpdateDto userUpdateDto) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
        updateProfileFields(user,userUpdateDto);
        userRepository.save(user);
        userEventSender.sendUserUpdated(user);
        return convertToUserDetailsDto(user);
    }

    /**
     * Update the profile picture of a user
     *
     * @param username The username of the user
     * @param file     The profile picture file to be uploaded
     * @return The updated UserDetailsDto
     * @throws UserNotFoundException If the user is not found
     */
    @Override
    public UserDetailsDto updateProfilePicture(String username, MultipartFile file) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));

        // Delete the old profile picture from S3
        String oldProfilePictureUrl = user.getProfile().getProfilePicture();
        if (oldProfilePictureUrl != null) {
            mediaService.deleteFile(oldProfilePictureUrl);
        }

        // Upload the new profile picture and update the user's profile
        String fileUrl = mediaService.uploadFile(file);
        user.getProfile().setProfilePicture(fileUrl);
        userRepository.save(user);

        return convertToUserDetailsDto(user);
    }


    /**
     * Update enabled status for a user account by their username
     *
     * @param username  The username of the user to be disabled
     * @param isEnabled the enabled status
     * @throws UserNotFoundException If the user with the specified username cannot be found
     */
    @Override
    public void updateUserEnabledStatus(String username, boolean isEnabled) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));

        user.setEnabled(isEnabled);
        userRepository.save(user);
    }

    /**
     * Converts a User entity to a UserDetailsDto
     *
     * @param user The User entity
     * @return UserDetailsDto
     */
    private UserDetailsDto convertToUserDetailsDto(User user) {
        Profile profile = user.getProfile();
        return UserDetailsDto.builder().email(user.getEmail()).username(user.getUsername()).fullName(profile.getFullName()).profilePicture(profile.getProfilePicture()).dateOfBirth(profile.getDateOfBirth()).gender(profile.getGender()).country(profile.getCountry()).cityOrRegion(profile.getCityOrRegion()).bio(profile.getBio()).socialMediaLinks(profile.getSocialMediaLinks()).isEnabled(user.isEnabled()).roles(user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList())).build();
    }

    /**
     * Updates the profile fields based on the provided updates map
     *
     * @param user          The user to be updated
     * @param userUpdateDto The fields to be updated
     */
    private void updateProfileFields(User user, UserUpdateDto userUpdateDto) {
        updateIfNotNull(userUpdateDto.getEmail(), () -> updateEmail(user, userUpdateDto.getEmail()));
        updateIfNotNull(userUpdateDto.getUsername(), () -> updateUsername(user, userUpdateDto.getUsername()));
        updateIfNotNull(userUpdateDto.getPassword(), () -> user.setPassword(passwordEncoder.encode(userUpdateDto.getPassword())));
        updateIfNotNull(userUpdateDto.getFullName(), () -> user.getProfile().setFullName(userUpdateDto.getFullName()));
        updateIfNotNull(userUpdateDto.getDateOfBirth(), () -> user.getProfile().setDateOfBirth(userUpdateDto.getDateOfBirth()));
        updateIfNotNull(userUpdateDto.getGender(), () -> user.getProfile().setGender(userUpdateDto.getGender()));
        updateIfNotNull(userUpdateDto.getCountry(), () -> user.getProfile().setCountry(userUpdateDto.getCountry()));
        updateIfNotNull(userUpdateDto.getCityOrRegion(), () -> user.getProfile().setCityOrRegion(userUpdateDto.getCityOrRegion()));
        updateIfNotNull(userUpdateDto.getBio(), () -> user.getProfile().setBio(userUpdateDto.getBio()));
        updateIfNotNull(userUpdateDto.getSocialMediaLinks(), () -> user.getProfile().setSocialMediaLinks(userUpdateDto.getSocialMediaLinks()));
    }

    private void updateIfNotNull(Object value, Runnable action) {
        Optional.ofNullable(value).ifPresent(v -> action.run());
    }

    private void updateUsername(User user, String username) {
        if (userRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(String.format("Username '%s' already exists", username));
        } else {
            user.setUsername(username);
        }
    }

    private void updateEmail(User user, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(String.format("Email '%s' already exists", email));
        } else {
            user.setEmail(email);
        }
    }
}
