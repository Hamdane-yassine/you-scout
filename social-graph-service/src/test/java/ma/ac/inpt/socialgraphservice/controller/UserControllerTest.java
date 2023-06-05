package ma.ac.inpt.socialgraphservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ac.inpt.socialgraphservice.config.SecurityTestConfig;
import ma.ac.inpt.socialgraphservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for UserController.
 */
@WebMvcTest(UserController.class)
@Import(SecurityTestConfig.class)
class UserControllerTest {


    private final List<String> users = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @BeforeEach
    void setup() {
        String username1 = "user1";
        users.add(username1);
        String username2 = "user2";
        users.add(username2);
    }

    @DisplayName("Test follow user endpoint")
    @Test
    void testFollowUser() throws Exception {
        String follower = "follower";
        String followee = "followee";

        doNothing().when(userService).followUser(follower, followee);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/" + follower + "/follow/" + followee))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).followUser(follower, followee);
    }

    @DisplayName("Test unfollow user endpoint")
    @Test
    void testUnfollowUser() throws Exception {
        String follower = "follower";
        String followee = "followee";

        doNothing().when(userService).unfollowUser(follower, followee);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/" + follower + "/unfollow/" + followee))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).unfollowUser(follower, followee);
    }

    @DisplayName("Test isFollowing endpoint")
    @Test
    void testIsFollowing() throws Exception {
        String follower = "follower";
        String followee = "followee";

        when(userService.isFollowing(follower, followee)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + follower + "/isFollowing/" + followee))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userService, times(1)).isFollowing(follower, followee);
    }

    @DisplayName("Test findFollowers endpoint")
    @Test
    void testFindFollowers() throws Exception {
        String username = "username";
        int page = 0;
        int size = 10;

        Page<String> pageUsers = new PageImpl<>(users);

        when(userService.findFollowers(username, page, size)).thenReturn(pageUsers);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + username + "/followers")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(pageUsers)));

        verify(userService, times(1)).findFollowers(username, page, size);
    }

    @DisplayName("Test findFollowing endpoint")
    @Test
    void testFindFollowing() throws Exception {
        String username = "username";
        int page = 0;
        int size = 10;

        Page<String> pageUsers = new PageImpl<>(users);

        when(userService.findFollowing(username, page, size)).thenReturn(pageUsers);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + username + "/following")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(pageUsers)));

        verify(userService, times(1)).findFollowing(username, page, size);
    }

    @DisplayName("Test blockUser endpoint")
    @Test
    void testBlockUser() throws Exception {
        String blocker = "blocker";
        String blocked = "blocked";

        doNothing().when(userService).blockUser(blocker, blocked);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/" + blocker + "/block/" + blocked))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).blockUser(blocker, blocked);
    }

    @DisplayName("Test unblockUser endpoint")
    @Test
    void testUnblockUser() throws Exception {
        String blocker = "blocker";
        String blocked = "blocked";

        doNothing().when(userService).unblockUser(blocker, blocked);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/" + blocker + "/unblock/" + blocked))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).unblockUser(blocker, blocked);
    }

    @DisplayName("Test isBlocking endpoint")
    @Test
    void testIsBlocking() throws Exception {
        String blocker = "blocker";
        String blocked = "blocked";

        when(userService.isBlocking(blocker, blocked)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + blocker + "/isBlocking/" + blocked))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userService, times(1)).isBlocking(blocker, blocked);
    }

    @DisplayName("Test getBlockedUsers endpoint")
    @Test
    void testGetBlockedUsers() throws Exception {
        String username = "username";
        int page = 0;
        int size = 10;

        Page<String> pageUsers = new PageImpl<>(users);

        when(userService.getBlockedUsers(username, page, size)).thenReturn(pageUsers);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + username + "/blocked")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(pageUsers)));

        verify(userService, times(1)).getBlockedUsers(username, page, size);
    }

    @DisplayName("Test countFollowers endpoint")
    @Test
    void testCountFollowers() throws Exception {
        String username = "username";

        when(userService.countFollowers(username)).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + username + "/followers/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(userService, times(1)).countFollowers(username);
    }

    @DisplayName("Test countFollowing endpoint")
    @Test
    void testCountFollowing() throws Exception {
        String username = "username";

        when(userService.countFollowing(username)).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + username + "/following/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(userService, times(1)).countFollowing(username);
    }

    @DisplayName("Test countBlockedUsers endpoint")
    @Test
    void testCountBlockedUsers() throws Exception {
        String username = "username";

        when(userService.countBlockedUsers(username)).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + username + "/blocked/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(userService, times(1)).countBlockedUsers(username);
    }

}
