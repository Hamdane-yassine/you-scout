package ma.ac.inpt.socialgraphservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ac.inpt.socialgraphservice.config.SecurityTestConfig;
import ma.ac.inpt.socialgraphservice.model.User;
import ma.ac.inpt.socialgraphservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityTestConfig.class)
class UserControllerTest {

    private final User user1 = new User();
    private final User user2 = new User();
    private final Set<User> users = new HashSet<>();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @BeforeEach
    void setup() {
        user1.setUsername("user1");
        user1.setId(1L);
        user2.setUsername("user2");
        user2.setId(2L);
        users.add(user1);
        users.add(user2);
    }

    @DisplayName("Test follow user endpoint")
    @Test
    void testFollowUser() throws Exception {
        String follower = "follower";
        String followee = "followee";

        doNothing().when(userService).followUser(follower, followee);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/" + follower + "/follow/" + followee))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).followUser(follower, followee);
    }

    @DisplayName("Test unfollow user endpoint")
    @Test
    void testUnfollowUser() throws Exception {
        String follower = "follower";
        String followee = "followee";

        doNothing().when(userService).unfollowUser(follower, followee);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/" + follower + "/unfollow/" + followee))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).unfollowUser(follower, followee);
    }

    @DisplayName("Test isFollowing endpoint")
    @Test
    void testIsFollowing() throws Exception {
        String follower = "follower";
        String followee = "followee";

        when(userService.isFollowing(follower, followee)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + follower + "/isFollowing/" + followee))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userService, times(1)).isFollowing(follower, followee);
    }

    @DisplayName("Test findFollowers endpoint")
    @Test
    void testFindFollowers() throws Exception {
        String username = "username";

        when(userService.findFollowers(username)).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + username + "/followers"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(users)));

        verify(userService, times(1)).findFollowers(username);
    }

    @DisplayName("Test findFollowing endpoint")
    @Test
    void testFindFollowing() throws Exception {
        String username = "username";

        when(userService.findFollowing(username)).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + username + "/following"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(users)));

        verify(userService, times(1)).findFollowing(username);
    }

    @DisplayName("Test blockUser endpoint")
    @Test
    void testBlockUser() throws Exception {
        String blocker = "blocker";
        String blocked = "blocked";

        doNothing().when(userService).blockUser(blocker, blocked);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/" + blocker + "/block/" + blocked))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).blockUser(blocker, blocked);
    }

    @DisplayName("Test unblockUser endpoint")
    @Test
    void testUnblockUser() throws Exception {
        String blocker = "blocker";
        String blocked = "blocked";

        doNothing().when(userService).unblockUser(blocker, blocked);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/" + blocker + "/unblock/" + blocked))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).unblockUser(blocker, blocked);
    }

    @DisplayName("Test isBlocking endpoint")
    @Test
    void testIsBlocking() throws Exception {
        String blocker = "blocker";
        String blocked = "blocked";

        when(userService.isBlocking(blocker, blocked)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + blocker + "/isBlocking/" + blocked))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userService, times(1)).isBlocking(blocker, blocked);
    }

    @DisplayName("Test getBlockedUsers endpoint")
    @Test
    void testGetBlockedUsers() throws Exception {
        String username = "username";

        when(userService.getBlockedUsers(username)).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + username + "/blocked"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(users)));

        verify(userService, times(1)).getBlockedUsers(username);
    }

    @DisplayName("Test countFollowers endpoint")
    @Test
    void testCountFollowers() throws Exception {
        String username = "username";

        when(userService.countFollowers(username)).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + username + "/followers/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(userService, times(1)).countFollowers(username);
    }

    @DisplayName("Test countFollowing endpoint")
    @Test
    void testCountFollowing() throws Exception {
        String username = "username";

        when(userService.countFollowing(username)).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + username + "/following/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(userService, times(1)).countFollowing(username);
    }

    @DisplayName("Test countBlockedUsers endpoint")
    @Test
    void testCountBlockedUsers() throws Exception {
        String username = "username";

        when(userService.countBlockedUsers(username)).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/" + username + "/blocked/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(userService, times(1)).countBlockedUsers(username);
    }

}
