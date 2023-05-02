package ma.ac.inpt.socialgraphservice.service;

import ma.ac.inpt.socialgraphservice.model.User;

import java.util.Set;

public interface UserService {

    void addUser(User user);

    void updateUser(User user);

    void deleteUser(Long id);

    void followUser(String followerUsername, String followeeUsername);

    void unfollowUser(String followerUsername, String followeeUsername);

    boolean isFollowing(String followerUsername, String followeeUsername);

    Set<User> findFollowers(String username);

    Set<User> findFollowing(String username);

    void blockUser(String blockerUsername, String blockedUsername);

    void unblockUser(String blockerUsername, String blockedUsername);

    boolean isBlocking(String blockerUsername, String blockedUsername);

    Set<User> getBlockedUsers(String username);


    int countFollowers(String username);

    int countFollowing(String username);

    int countBlockedUsers(String username);

}

