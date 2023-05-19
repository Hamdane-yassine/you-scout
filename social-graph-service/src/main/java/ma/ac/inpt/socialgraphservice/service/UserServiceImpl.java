package ma.ac.inpt.socialgraphservice.service;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.socialgraphservice.exception.UserAlreadyExistsException;
import ma.ac.inpt.socialgraphservice.exception.UserNotFoundException;
import ma.ac.inpt.socialgraphservice.exception.UserOperationNotAllowedException;
import ma.ac.inpt.socialgraphservice.model.User;
import ma.ac.inpt.socialgraphservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void addUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistsException("User already exists");
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        if (!userRepository.existsById(user.getId())) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.save(user);
    }

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

    @Override
    public boolean isFollowing(String followerUsername, String followeeUsername) {
        checkUserExistsByUsername(followerUsername);
        checkUserExistsByUsername(followeeUsername);
        return userRepository.isFollowing(followerUsername, followeeUsername);
    }

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

    @Override
    public Set<User> findFollowers(String username) {
        checkUserExistsByUsername(username);
        return userRepository.findFollowers(username);
    }

    @Override
    public Set<User> findFollowing(String username) {
        checkUserExistsByUsername(username);
        return userRepository.findFollowing(username);
    }

    @Override
    public Set<User> getBlockedUsers(String username) {
        checkUserExistsByUsername(username);
        return userRepository.getBlockedUsers(username);
    }

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

    @Override
    public boolean isBlocking(String blockerUsername, String blockedUsername) {
        checkUserExistsByUsername(blockerUsername);
        checkUserExistsByUsername(blockedUsername);
        return userRepository.isBlocking(blockerUsername, blockedUsername);
    }

    @Override
    public int countFollowers(String username) {
        checkUserExistsByUsername(username);
        return userRepository.countFollowers(username);
    }

    @Override
    public int countFollowing(String username) {
        checkUserExistsByUsername(username);
        return userRepository.countFollowing(username);
    }

    @Override
    public int countBlockedUsers(String username) {
        checkUserExistsByUsername(username);
        return userRepository.countBlockedUsers(username);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
        userRepository.deleteById(user.getId());
    }

    private void checkUserExistsByUsername(String username) {
        if (!userRepository.existsByUsername(username)) {
            throw new UserNotFoundException("User not found");
        }
    }

    private void checkFollowingNotAllowed(String followerUsername, String followeeUsername) {
        if (isFollowing(followerUsername, followeeUsername)) {
            throw new UserOperationNotAllowedException("Already following this user");
        }
    }

    private void checkBlockingNotAllowed(String blockerUsername, String blockedUsername) {
        if (isBlocking(blockerUsername, blockedUsername)) {
            throw new UserOperationNotAllowedException("User is already blocked");
        }
    }
}