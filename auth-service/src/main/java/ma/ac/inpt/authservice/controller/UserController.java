package ma.ac.inpt.authservice.controller;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.authservice.payload.UserDetailsDto;
import ma.ac.inpt.authservice.payload.UserUpdateDto;
import ma.ac.inpt.authservice.service.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    /**
     * Get all users
     *
     * @param page page number (default is 0)
     * @param size page size (default is 20)
     * @return ResponseEntity<Page < UserDetailsDto>>
     */
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserDetailsDto>> getAllUsers(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "20") Integer size) {
        return ResponseEntity.ok(userServiceImpl.getAllUsers(page, size));
    }

    /**
     * Partially update user by username
     *
     * @param username  the username
     * @param userUpdateDto The dto for request updating
     * @return ResponseEntity<UserDetailsDto>
     */
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PatchMapping("/{username}")
    public ResponseEntity<UserDetailsDto> UpdateUserByUsername(@PathVariable String username, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(userServiceImpl.UpdateUserByUsername(username, userUpdateDto));
    }

    /**
     * Delete user by username
     *
     * @param username the username
     * @return ResponseEntity<Void>
     */
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUserByUsername(@PathVariable String username) {
        userServiceImpl.deleteUserByUsername(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Get user's profile by username
     *
     * @param username the username
     * @return ResponseEntity<UserDetailsDto>
     */
    @GetMapping("/{username}/profile")
    public ResponseEntity<UserDetailsDto> getProfileByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userServiceImpl.getUserDetailsByUsername(username));
    }

    /**
     * Disable user by username
     *
     * @param username the username
     * @return ResponseEntity<Void>
     */
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{username}/disable")
    public ResponseEntity<Void> disableUserByUsername(@PathVariable String username) {
        userServiceImpl.updateUserEnabledStatus(username, false);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Enable user by username
     *
     * @param username the username
     * @return ResponseEntity<Void>
     */
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    @PutMapping("/{username}/enable")
    public ResponseEntity<Void> enableUserByUsername(@PathVariable String username) {
        userServiceImpl.updateUserEnabledStatus(username, true);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    /**
     * Get current user's profile
     *
     * @param principal the authenticated user
     * @return ResponseEntity<UserDetailsDto>
     */
    @GetMapping("/me/profile")
    public ResponseEntity<UserDetailsDto> getCurrentUserProfile(Principal principal) {
        return ResponseEntity.ok(userServiceImpl.getUserDetailsByUsername(principal.getName()));
    }

    /**
     *  update current user
     *
     * @param principal the authenticated user
     * @param userUpdateDto The dto for request updating
     * @return ResponseEntity<UserDetailsDto>
     */
    @PatchMapping("/me/profile")
    public ResponseEntity<UserDetailsDto> UpdateCurrentUser(Principal principal, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(userServiceImpl.UpdateUserByUsername(principal.getName(), userUpdateDto));
    }

    /**
     * Update current user's profile picture
     *
     * @param principal the authenticated user
     * @param file           the profile picture file
     * @return ResponseEntity<UserDetailsDto>
     */
    @PostMapping("/me/profile/picture")
    public ResponseEntity<UserDetailsDto> updateProfilePicture(Principal principal, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(userServiceImpl.updateProfilePicture(principal.getName(), file));
    }
}