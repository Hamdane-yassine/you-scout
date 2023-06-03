package ma.ac.inpt.socialgraphservice.service;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.socialgraphservice.exception.UserAlreadyExistsException;
import ma.ac.inpt.socialgraphservice.exception.UserNotFoundException;
import ma.ac.inpt.socialgraphservice.exception.UserOperationNotAllowedException;
import ma.ac.inpt.socialgraphservice.model.User;
import ma.ac.inpt.socialgraphservice.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service implementation for managing User entities.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Adds a new user.
     *
     * @param user the user to add
     */
    @Override
    @Transactional
    public void addUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("User already exists");
        }
        userRepository.save(user);
    }

    /**
     * Updates an existing user.
     *
     * @param user the user to update
     */
    @Override
    @Transactional
    public void updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.save(user);
    }

    /**
     * Follows a user.
     *
     * @param followerUsername the username of the follower
     * @param followeeUsername the username of the followee
     */
    @Override
    @Transactional
    public void followUser(String followerUsername, String followeeUsername) {
        checkUserExistsByUsername(followerUsername);
        checkUserExistsByUsername(followeeUsername);
        checkFollowingNotAllowed(followerUsername, followeeUsername);

        if (isBlocking(followerUsername, followeeUsername) || isBlocking(followeeUsername, followerUsername)) {
            throw new UserOperationNotAllowedException("Cannot follow a blocked user or a user who has blocked you");
        }

        userRepository.followUser(followerUsername, followeeUsername);
    }

    /**
     * Unfollows a user.
     *
     * @param followerUsername the username of the follower
     * @param followeeUsername the username of the followee
     */
    @Override
    @Transactional
    public void unfollowUser(String followerUsername, String followeeUsername) {
        checkUserExistsByUsername(followerUsername);
        checkUserExistsByUsername(followeeUsername);

        if (!isFollowing(followerUsername, followeeUsername)) {
            throw new UserOperationNotAllowedException("Cannot unfollow a user you are not following");
        }

        userRepository.unfollowUser(followerUsername, followeeUsername);
    }

    /**
     * Checks if a user is following another user.
     *
     * @param followerUsername the username of the follower
     * @param followeeUsername the username of the followee
     * @return true if the follower is following the followee, false otherwise
     */
    @Override
    public boolean isFollowing(String followerUsername, String followeeUsername) {
        checkUserExistsByUsername(followerUsername);
        checkUserExistsByUsername(followeeUsername);
        return userRepository.isFollowing(followerUsername, followeeUsername);
    }

    /**
     * Blocks a user.
     *
     * @param blockerUsername the username of the blocker
     * @param blockedUsername the username of the blocked user
     */
    @Override
    @Transactional
    public void blockUser(String blockerUsername, String blockedUsername) {
        checkUserExistsByUsername(blockerUsername);
        checkUserExistsByUsername(blockedUsername);
        checkBlockingNotAllowed(blockerUsername, blockedUsername);

        if (isFollowing(blockerUsername, blockedUsername)) {
            unfollowUser(blockerUsername, blockedUsername);
        }
        if (isFollowing(blockedUsername, blockerUsername)) {
            unfollowUser(blockedUsername, blockerUsername);
        }
        userRepository.blockUser(blockerUsername, blockedUsername);
    }

    /**
     * Finds the followers of a user.
     *
     * @param username the username of the user
     * @param page     the page number
     * @param size     the page size
     * @return a Page of follower usernames
     */
    @Override
    public Page<String> findFollowers(String username, Integer page, Integer size) {
        checkUserExistsByUsername(username);
        return userRepository.findFollowerUsernamesByUsername(username, PageRequest.of(page, size));
    }

    /**
     * Finds the users being followed by a user.
     *
     * @param username the username of the user
     * @param page     the page number
     * @param size     the page size
     * @return a Page of following usernames
     */
    @Override
    public Page<String> findFollowing(String username, Integer page, Integer size) {
        checkUserExistsByUsername(username);
        return userRepository.findFollowingUsernamesByUsername(username, PageRequest.of(page, size));
    }

    /**
     * Finds the blocked users of a user.
     *
     * @param username the username of the user
     * @param page     the page number
     * @param size     the page size
     * @return a Page of blocked usernames
     */
    @Override
    public Page<String> getBlockedUsers(String username, Integer page, Integer size) {
        checkUserExistsByUsername(username);
        return userRepository.getBlockedUsernamesByUsername(username, PageRequest.of(page, size));
    }

    /**
     * Unblocks a user.
     *
     * @param blockerUsername the username of the blocker
     * @param blockedUsername the username of the blocked user
     */
    @Override
    @Transactional
    public void unblockUser(String blockerUsername, String blockedUsername) {
        checkUserExistsByUsername(blockerUsername);
        checkUserExistsByUsername(blockedUsername);

        if (!isBlocking(blockerUsername, blockedUsername)) {
            throw new UserOperationNotAllowedException("Cannot unblock a user who is not blocked");
        }

        userRepository.unblockUser(blockerUsername, blockedUsername);
    }

    /**
     * Checks if a user is blocking another user.
     *
     * @param blockerUsername the username of the blocker
     * @param blockedUsername the username of the blocked user
     * @return true if the blocker is blocking the blocked user, false otherwise
     */
    @Override
    public boolean isBlocking(String blockerUsername, String blockedUsername) {
        checkUserExistsByUsername(blockerUsername);
        checkUserExistsByUsername(blockedUsername);
        return userRepository.isBlocking(blockerUsername, blockedUsername);
    }

    /**
     * Counts the number of followers of a user.
     *
     * @param username the username of the user
     * @return the number of followers
     */
    @Override
    public int countFollowers(String username) {
        checkUserExistsByUsername(username);
        return userRepository.countFollowers(username);
    }

    /**
     * Counts the number of users being followed by a user.
     *
     * @param username the username of the user
     * @return the number of following users
     */
    @Override
    public int countFollowing(String username) {
        checkUserExistsByUsername(username);
        return userRepository.countFollowing(username);
    }

    /**
     * Counts the number of blocked users of a user.
     *
     * @param username the username of the user
     * @return the number of blocked users
     */
    @Override
    public int countBlockedUsers(String username) {
        checkUserExistsByUsername(username);
        return userRepository.countBlockedUsers(username);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     */
    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
        userRepository.deleteById(user.getId());
    }

    /**
     * Checks if a user exists based on their username.
     *
     * @param username the username to check
     */
    private void checkUserExistsByUsername(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException("User not found");
        }
    }

    /**
     * Checks if following a user is not allowed.
     *
     * @param followerUsername the username of the follower
     * @param followeeUsername the username of the followee
     */
    private void checkFollowingNotAllowed(String followerUsername, String followeeUsername) {
        if (isFollowing(followerUsername, followeeUsername)) {
            throw new UserOperationNotAllowedException("Already following this user");
        }
    }

    /**
     * Checks if blocking a user is not allowed.
     *
     * @param blockerUsername the username of the blocker
     * @param blockedUsername the username of the blocked user
     */
    private void checkBlockingNotAllowed(String blockerUsername, String blockedUsername) {
        if (isBlocking(blockerUsername, blockedUsername)) {
            throw new UserOperationNotAllowedException("User is already blocked");
        }
    }
}