package ma.ac.inpt.authservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.dto.*;
import ma.ac.inpt.authservice.service.user.UserServiceImpl;
import ma.ac.inpt.authservice.util.ValidImage;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;

/**
 * Controller class for managing user data.
 * Provides endpoints for getting, updating and deleting user data.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserServiceImpl userServiceImpl;

    /**
     * Endpoint for getting all users.
     * Returns HTTP 200 OK status with a list of UserDetailsDto on successful retrieval.
     *
     * @param page The page number.
     * @param size The page size.
     * @return A response entity with a list of UserDetailsDto.
     */
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserDetailsDto>> getAllUsers(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "20") Integer size) {
        log.info("Received request to get all users with page {} and size {}", page, size);
        Page<UserDetailsDto> users = userServiceImpl.getAllUsers(page, size);
        return ResponseEntity.ok(users);
    }

    /**
     * Endpoint for deleting a user by username.
     * Returns HTTP 204 NO CONTENT status on successful deletion.
     *
     * @param username The username of the user to be deleted.
     * @return A response entity with no content.
     */
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUserByUsername(@PathVariable String username) {
        log.info("Received request to delete user with username {}", username);
        userServiceImpl.deleteUserByUsername(username);
        log.info("Deleted user with username {}", username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint for getting a user profile by username.
     * Returns HTTP 200 OK status with a UserDetailsDto on successful retrieval.
     *
     * @param username The username of the user to get profile for.
     * @return A response entity with the UserDetailsDto.
     */
    @GetMapping("/{username}/profile")
    public ResponseEntity<UserDetailsDto> getProfileByUsername(@PathVariable String username) {
        log.info("Received request to get profile for username {}", username);
        UserDetailsDto userDetails = userServiceImpl.getUserDetailsByUsername(username);
        return ResponseEntity.ok(userDetails);
    }

    /**
     * Endpoint for disabling a user by username.
     * Returns HTTP 204 NO CONTENT status on successful disable.
     *
     * @param username The username of the user to be disabled.
     * @return A response entity with no content.
     */
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{username}/disable")
    public ResponseEntity<Void> disableUserByUsername(@PathVariable String username) {
        log.info("Received request to disable user with username {}", username);
        userServiceImpl.updateUserEnabledStatus(username, false);
        log.info("Disabled user with username {}", username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint for enabling a user by username.
     * Returns HTTP 204 NO CONTENT status on successful enable.
     *
     * @param username The username of the user to be enabled.
     * @return A response entity with no content.
     */
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{username}/enable")
    public ResponseEntity<Void> enableUserByUsername(@PathVariable String username) {
        log.info("Received request to enable user with username {}", username);
        userServiceImpl.updateUserEnabledStatus(username, true);
        log.info("Enabled user with username {}", username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Endpoint for getting the profile of the currently authenticated user.
     * Returns HTTP 200 OK status with the UserDetailsDto on successful retrieval.
     *
     * @param principal The principal object representing the authenticated user.
     * @return A response entity with the UserDetailsDto.
     */
    @GetMapping("/me/profile")
    public ResponseEntity<UserDetailsDto> getCurrentUserProfile(Principal principal) {
        log.info("Received request to get current user profile");
        UserDetailsDto userDetails = userServiceImpl.getUserDetailsByUsername(principal.getName());
        return ResponseEntity.ok(userDetails);
    }

    /**
     * Endpoint for updating the profile of the currently authenticated user.
     * Validates user update request using the UserUpdateDto.
     * Returns HTTP 200 OK status with the updated UserDetailsDto on successful update.
     *
     * @param principal The principal object representing the authenticated user.
     * @param profileUpdateRequest The updated user data.
     * @return A response entity with the updated UserDetailsDto.
     */
    @PatchMapping("/me/profile")
    public ResponseEntity<ProfileUpdateResponse> updateCurrentUserProfile(Principal principal, @Valid @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        log.info("Received request to update current user profile");
        ProfileUpdateResponse updatedUser = userServiceImpl.updateProfileByUsername(principal.getName(), profileUpdateRequest);
        log.info("Updated current user profile");
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserUpdateResponse> updateCurrentUserDetails(Principal principal, @Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        log.info("Received request to update current user profile");
        UserUpdateResponse updatedUser = userServiceImpl.updateUserByUsername(principal.getName(), userUpdateRequest);
        log.info("Updated current user profile");
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Endpoint for updating the profile picture of the currently authenticated user.
     * Returns HTTP 200 OK status with the updated UserDetailsDto on successful update.
     *
     * @param principal The principal object representing the authenticated user.
     * @param file The profile picture file.
     * @return A response entity with the updated UserDetailsDto.
     */
    @PostMapping("/me/profile/picture")
    public ResponseEntity<ProfilePictureUpdateResponse> updateProfilePicture(Principal principal, @ValidImage @RequestPart("file") MultipartFile file) {
        log.info("Received request to update profile picture for the current user");
        ProfilePictureUpdateResponse profilePictureUpdateResponse = userServiceImpl.updateProfilePicture(principal.getName(), file);
        log.info("Updated profile picture for the current user");
        return ResponseEntity.ok(profilePictureUpdateResponse);
    }
}