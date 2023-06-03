package ma.ac.inpt.socialgraphservice.service;

import ma.ac.inpt.socialgraphservice.model.User;
import org.springframework.data.domain.Page;

/**
 * Service interface for managing User entities.
 */
public interface UserService {

    /**
     * Adds a new user.
     *
     * @param user the user to add
     */
    void addUser(User user);

    /**
     * Updates an existing user.
     *
     * @param user the user to update
     */
    void updateUser(User user);

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     */
    void deleteUser(Long id);

    /**
     * Follows a user by creating the FOLLOWS relationship between the follower and followee users.
     *
     * @param followerUsername the username of the follower user
     * @param followeeUsername the username of the followee user
     */
    void followUser(String followerUsername, String followeeUsername);

    /**
     * Unfollows a user by deleting the FOLLOWS relationship between the follower and followee users.
     *
     * @param followerUsername the username of the follower user
     * @param followeeUsername the username of the followee user
     */
    void unfollowUser(String followerUsername, String followeeUsername);

    /**
     * Checks if a user is following another user.
     *
     * @param followerUsername the username of the follower user
     * @param followeeUsername the username of the followee user
     * @return true if the follower user is following the followee user, false otherwise
     */
    boolean isFollowing(String followerUsername, String followeeUsername);

    /**
     * Retrieves a page of follower usernames for a given username.
     *
     * @param username the username of the user
     * @param page     the page number
     * @param size     the page size
     * @return a page of follower usernames
     */
    Page<String> findFollowers(String username, Integer page, Integer size);

    /**
     * Retrieves a page of following usernames for a given username.
     *
     * @param username the username of the user
     * @param page     the page number
     * @param size     the page size
     * @return a page of following usernames
     */
    Page<String> findFollowing(String username, Integer page, Integer size);

    /**
     * Blocks a user by creating the BLOCKS relationship between the blocker and blocked users.
     *
     * @param blockerUsername the username of the blocker user
     * @param blockedUsername the username of the blocked user
     */
    void blockUser(String blockerUsername, String blockedUsername);

    /**
     * Unblocks a user by deleting the BLOCKS relationship between the blocker and blocked users.
     *
     * @param blockerUsername the username of the blocker user
     * @param blockedUsername the username of the blocked user
     */
    void unblockUser(String blockerUsername, String blockedUsername);

    /**
     * Checks if a user is blocking another user.
     *
     * @param blockerUsername the username of the blocker user
     * @param blockedUsername the username of the blocked user
     * @return true if the blocker user is blocking the blocked user, false otherwise
     */
    boolean isBlocking(String blockerUsername, String blockedUsername);

    /**
     * Retrieves a page of blocked usernames for a given username.
     *
     * @param username the username of the user
     * @param page     the page number
     * @param size     the page size
     * @return a page of blocked usernames
     */
    Page<String> getBlockedUsers(String username, Integer page, Integer size);

    /**
     * Counts the number of followers for a given username.
     *
     * @param username the username of the user
     * @return the count of followers
     */
    int countFollowers(String username);

    /**
     * Counts the number of users that a given username is following.
     *
     * @param username the username of the user
     * @return the count of following users
     */
    int countFollowing(String username);

    /**
     * Counts the number of blocked users for a given username.
     *
     * @param username the username of the user
     * @return the count of blocked users
     */
    int countBlockedUsers(String username);

}

