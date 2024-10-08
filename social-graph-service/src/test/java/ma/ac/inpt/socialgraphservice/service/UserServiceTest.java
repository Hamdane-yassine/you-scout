package ma.ac.inpt.socialgraphservice.service;

import ma.ac.inpt.socialgraphservice.exception.UserAlreadyExistsException;
import ma.ac.inpt.socialgraphservice.exception.UserNotFoundException;
import ma.ac.inpt.socialgraphservice.exception.UserOperationNotAllowedException;
import ma.ac.inpt.socialgraphservice.model.User;
import ma.ac.inpt.socialgraphservice.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test class for UserService.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Test Add User")
    void testAddUser() {
        // Given
        User user = new User();
        user.setUsername("username");

        // When
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        userService.addUser(user);

        // Verify
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test Update User")
    void testUpdateUser() {
        // Given
        User user = new User();
        user.setId(1L);

        // When
        when(userRepository.existsById(anyLong())).thenReturn(true);
        userService.updateUser(user);

        // Verify
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Test Follow User")
    void testFollowUser() {
        // When
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        userService.followUser("username1", "username2");

        // Verify
        verify(userRepository, times(1)).followUser("username1", "username2");
    }

    @Test
    @DisplayName("Test Unfollow User")
    void testUnfollowUser() {
        // When
        when(userRepository.isFollowing(anyString(), anyString())).thenReturn(true);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        userService.unfollowUser("username1", "username2");

        // Verify
        verify(userRepository, times(1)).unfollowUser("username1", "username2");
    }

    @Test
    @DisplayName("Test Is Following")
    void testIsFollowing() {
        // When
        when(userRepository.isFollowing(anyString(), anyString())).thenReturn(true);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        boolean result = userService.isFollowing("username1", "username2");

        // Verify
        assertTrue(result);
    }

    @Test
    @DisplayName("Test Block User")
    void testBlockUser() {
        // When
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        userService.blockUser("username1", "username2");

        // Verify
        verify(userRepository, times(1)).blockUser("username1", "username2");
    }

    @Test
    @DisplayName("Test Unblock User")
    void testUnblockUser() {
        // When
        when(userRepository.isBlocking(anyString(), anyString())).thenReturn(true);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        userService.unblockUser("username1", "username2");

        // Verify
        verify(userRepository, times(1)).unblockUser("username1", "username2");
    }

    @Test
    @DisplayName("Test Is Blocking")
    void testIsBlocking() {
        // When
        when(userRepository.isBlocking(anyString(), anyString())).thenReturn(true);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        boolean result = userService.isBlocking("username1", "username2");

        // Verify
        assertTrue(result);
    }

    @Test
    @DisplayName("Test Find Followers")
    void testFindFollowers() {
        // Given
        String username = "username";
        int page = 0;
        int size = 10;
        Page<String> pageOfUsernames = new PageImpl<>(List.of("follower1"), PageRequest.of(page, size), 1);

        // When
        when(userRepository.existsByUsername(username)).thenReturn(true);
        when(userRepository.findFollowerUsernamesByUsername(username, PageRequest.of(page, size)))
                .thenReturn(pageOfUsernames);
        Page<String> result = userService.findFollowers(username, page, size);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Test Find Following")
    void testFindFollowing() {
        // Given
        String username = "username";
        Page<String> expectedPage = new PageImpl<>(List.of("following1"), PageRequest.of(0, 10), 1);

        // When
        when(userRepository.existsByUsername(username)).thenReturn(true);
        when(userRepository.findFollowingUsernamesByUsername(username, PageRequest.of(0, 10)))
                .thenReturn(expectedPage);
        Page<String> result = userService.findFollowing(username, 0, 10);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Test Get Blocked Users")
    void testGetBlockedUsers() {
        // Given
        String username = "username";
        Page<String> expectedPage = new PageImpl<>(List.of("blocked1"), PageRequest.of(0, 10), 1);

        // When
        when(userRepository.existsByUsername(username)).thenReturn(true);
        when(userRepository.getBlockedUsernamesByUsername(username, PageRequest.of(0, 10)))
                .thenReturn(expectedPage);
        Page<String> result = userService.getBlockedUsers(username, 0, 10);

        // Verify
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("Test Delete User")
    void testDeleteUser() {
        // Given
        User user = new User();
        user.setId(1L);

        // When
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        userService.deleteUser(1L);

        // Verify
        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Test Count Followers")
    void testCountFollowers() {
        // When
        when(userRepository.countFollowers(anyString())).thenReturn(1);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        int result = userService.countFollowers("username");

        // Verify
        assertEquals(1, result);
    }

    @Test
    @DisplayName("Test Count Following")
    void testCountFollowing() {
        // When
        when(userRepository.countFollowing(anyString())).thenReturn(1);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        int result = userService.countFollowing("username");

        // Verify
        assertEquals(1, result);
    }

    @Test
    @DisplayName("Test Count Blocked Users")
    void testCountBlockedUsers() {
        // When
        when(userRepository.countBlockedUsers(anyString())).thenReturn(1);
        when(userRepository.existsByUsername(anyString())).thenReturn(true);
        int result = userService.countBlockedUsers("username");

        // Verify
        assertEquals(1, result);
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when trying to add a user that already exists")
    void testAddUser_UserAlreadyExists() {
        // Given
        User user = new User();
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        // Verify
        assertThrows(UserAlreadyExistsException.class, () -> userService.addUser(user));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when trying to update a user that doesn't exist")
    void testUpdateUser_UserNotFound() {
        // Given
        User user = new User();
        user.setId(1L);
        when(userRepository.existsById(user.getId())).thenReturn(false);

        // Verify
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(user));
    }

    @Test
    @DisplayName("Should throw UserOperationNotAllowedException when trying to follow user that's already followed")
    void testFollowUser_UserAlreadyFollowed() {
        // Given
        String followerUsername = "Follower";
        String followeeUsername = "Followee";
        when(userRepository.existsByUsername(followerUsername)).thenReturn(true);
        when(userRepository.existsByUsername(followeeUsername)).thenReturn(true);
        when(userRepository.isFollowing(followerUsername, followeeUsername)).thenReturn(true);

        // Verify
        assertThrows(UserOperationNotAllowedException.class, () -> userService.followUser(followerUsername, followeeUsername));
    }

    @Test
    @DisplayName("Should throw UserOperationNotAllowedException when trying to unfollow user that's not followed")
    void testUnfollowUser_UserNotFollowed() {
        // Given
        String followerUsername = "Follower";
        String followeeUsername = "Followee";
        when(userRepository.existsByUsername(followerUsername)).thenReturn(true);
        when(userRepository.existsByUsername(followeeUsername)).thenReturn(true);
        when(userRepository.isFollowing(followerUsername, followeeUsername)).thenReturn(false);

        // Verify
        assertThrows(UserOperationNotAllowedException.class, () -> userService.unfollowUser(followerUsername, followeeUsername));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when trying to delete user that doesn't exist")
    void testDeleteUser_UserNotFound() {
        // Given
        Long id = 1L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        // Verify
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(id));
    }

    @Test
    @DisplayName("Should throw UserOperationNotAllowedException when trying to unblock user that's not blocked")
    void testUnblockUser_UserNotBlocked() {
        // Given
        String blockerUsername = "Blocker";
        String blockedUsername = "Blocked";
        when(userRepository.existsByUsername(blockerUsername)).thenReturn(true);
        when(userRepository.existsByUsername(blockedUsername)).thenReturn(true);
        when(userRepository.isBlocking(blockerUsername, blockedUsername)).thenReturn(false);

        // Verify
        assertThrows(UserOperationNotAllowedException.class, () -> userService.unblockUser(blockerUsername, blockedUsername));
    }

    @Test
    @DisplayName("Should throw UserOperationNotAllowedException when trying to block user that's already blocked")
    void testBlockUser_UserAlreadyBlocked() {
        // Given
        String blockerUsername = "Blocker";
        String blockedUsername = "Blocked";
        when(userRepository.existsByUsername(blockerUsername)).thenReturn(true);
        when(userRepository.existsByUsername(blockedUsername)).thenReturn(true);
        when(userRepository.isBlocking(blockerUsername, blockedUsername)).thenReturn(true);

        // Verify
        assertThrows(UserOperationNotAllowedException.class, () -> userService.blockUser(blockerUsername, blockedUsername));
    }
}


