package ma.ac.inpt.authservice.service;

import ma.ac.inpt.authservice.exception.UserNotFoundException;
import ma.ac.inpt.authservice.payload.UserDetailsDto;
import ma.ac.inpt.authservice.payload.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    /**
     * Retrieve all users
     *
     * @param page The page number
     * @param size The number of items per page
     * @return Page of UserDetailsDto
     */
    Page<UserDetailsDto> getAllUsers(Integer page, Integer size);

    /**
     * Delete a user by their username
     *
     * @param username The username of the user
     * @throws UserNotFoundException If the user is not found
     */
    void deleteUserByUsername(String username);

    /**
     * Retrieve a user's profile by their username
     *
     * @param username The username of the user
     * @return UserDetailsDto
     * @throws UserNotFoundException If the user is not found
     */
    UserDetailsDto getUserDetailsByUsername(String username);

    /**
     * Partially update a user's profile by their username
     *
     * @param username  The username of the user
     * @param userUpdateDto The dto for request updating
     * @return UserDetailsDto
     * @throws UserNotFoundException If the user is not found
     */
    UserDetailsDto UpdateUserByUsername(String username, UserUpdateDto userUpdateDto);

    /**
     * Update the profile picture of a user
     *
     * @param username The username of the user
     * @param file     The profile picture file to be uploaded
     * @return The updated UserDetailsDto
     * @throws UserNotFoundException If the user is not found
     */
    UserDetailsDto updateProfilePicture(String username, MultipartFile file);

    /**
     * Update enabled status for a user account by their username
     *
     * @param username  The username of the user to be disabled
     * @param isEnabled the enabled status
     * @throws UserNotFoundException If the user with the specified username cannot be found
     */
    void updateUserEnabledStatus(String username, boolean isEnabled);
}

