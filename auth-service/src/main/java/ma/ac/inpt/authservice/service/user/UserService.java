package ma.ac.inpt.authservice.service.user;

import ma.ac.inpt.authservice.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

/**
 * UserService is an interface that provides methods for retrieving and manipulating user data.
 */
public interface UserService {


    Page<UserDetailsDto> getUsersByUsername(String username, Integer page, Integer size);
    /**
     * Retrieves all users from the database.
     *
     * @param page the page number to retrieve
     * @param size the number of users to retrieve per page
     * @return a Page object containing the requested users
     */
    Page<UserDetailsDto> getAllUsers(Integer page, Integer size);

    /**
     * Deletes a user by their username.
     *
     * @param username the username of the user to be deleted
     */
    void deleteUserByUsername(String username);

    /**
     * Retrieves the details of a user by their username.
     *
     * @param username the username of the user to retrieve
     * @return a UserDetailsDto object containing the user's details
     */
    UserDetailsDto getUserDetailsByUsername(String username);

    /**
     * Updates a user's details by their username.
     *
     * @param username          the username of the user to update
     * @param userUpdateRequest the DTO containing the updated user details
     * @return a UserDetailsDto object containing the updated user's details
     */
    UserUpdateResponse updateUserByUsername(String username, UserUpdateRequest userUpdateRequest);

    /**
     * Updates a user's profile by their username.
     *
     * @param username               the username of the user to update
     * @param profileUpdateRequest   the DTO containing the updated user details
     * @return a UserDetailsDto object containing the updated user's details
     */
    ProfileUpdateResponse updateProfileByUsername(String username, ProfileUpdateRequest profileUpdateRequest);

    /**
     * Updates a user's profile picture by their username.
     *
     * @param username the username of the user to update
     * @param file     the profile picture file to update
     * @return a UserDetailsDto object containing the updated user's details
     */
    ProfilePictureUpdateResponse updateProfilePicture(String username, MultipartFile file);

    /**
     * Updates a user's enabled status by their username.
     *
     * @param username  the username of the user to update
     * @param isEnabled the new enabled status
     */
    void updateUserEnabledStatus(String username, boolean isEnabled);
}


