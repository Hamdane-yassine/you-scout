package ma.ac.inpt.socialgraphservice.mapper;

import ma.ac.inpt.socialgraphservice.model.User;
import ma.ac.inpt.socialgraphservice.payload.UserEventPayload;
import ma.ac.inpt.socialgraphservice.payload.UserEventType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserMapperTest {

    @Test
    @DisplayName("Test mapping from UserEventPayload to User")
    void testUserEventPayloadToUser() {
        // Given
        UserEventPayload payload = new UserEventPayload();
        payload.setId(1L);
        payload.setUsername("username");
        payload.setUserEventType(UserEventType.CREATED);

        // When
        User user = UserMapper.INSTANCE.userEventPayloadToUser(payload);

        // Then
        assertEquals(payload.getId(), user.getId());
        assertEquals(payload.getUsername(), user.getUsername());
        assertNull(user.getFollowing());
        assertNull(user.getFollowers());
        assertNull(user.getBlockedUsers());
    }

    @Test
    @DisplayName("Test mapping from User to UserEventPayload")
    void testUserToUserEventPayload() {
        // Given
        User user = new User();
        user.setId(1L);
        user.setUsername("username");
        user.setFollowing(new HashSet<>());
        user.setFollowers(new HashSet<>());
        user.setBlockedUsers(new HashSet<>());

        // When
        UserEventPayload payload = UserMapper.INSTANCE.userToUserEventPayload(user);

        // Then
        assertEquals(user.getId(), payload.getId());
        assertEquals(user.getUsername(), payload.getUsername());
        // UserEventType in the payload is null because User object does not contain eventType
        assertNull(payload.getUserEventType());
    }
}

