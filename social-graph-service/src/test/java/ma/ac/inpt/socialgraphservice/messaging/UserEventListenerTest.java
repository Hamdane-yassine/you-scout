package ma.ac.inpt.socialgraphservice.messaging;

import ma.ac.inpt.socialgraphservice.model.User;
import ma.ac.inpt.socialgraphservice.payload.UserEventPayload;
import ma.ac.inpt.socialgraphservice.payload.UserEventType;
import ma.ac.inpt.socialgraphservice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserEventListenerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserEventListener userEventListener;

    @Test
    @DisplayName("Test if consume method adds a user on CREATED event type")
    public void testConsume_UserCreated() {
        // Given
        UserEventPayload payload = new UserEventPayload();
        payload.setUserEventType(UserEventType.CREATED);
        payload.setId(1L);
        payload.setUsername("username");

        // When
        userEventListener.consume(payload);

        // Then
        verify(userService).addUser(any(User.class));
    }

    @Test
    @DisplayName("Test if consume method updates a user on UPDATED event type")
    public void testConsume_UserUpdated() {
        // Given
        UserEventPayload payload = new UserEventPayload();
        payload.setUserEventType(UserEventType.UPDATED);
        payload.setId(1L);
        payload.setUsername("username");

        // When
        userEventListener.consume(payload);

        // Then
        verify(userService).updateUser(any(User.class));
    }

    @Test
    @DisplayName("Test if consume method deletes a user on DELETED event type")
    public void testConsume_UserDeleted() {
        // Given
        UserEventPayload payload = new UserEventPayload();
        payload.setUserEventType(UserEventType.DELETED);
        payload.setId(1L);
        payload.setUsername("username");

        // When
        userEventListener.consume(payload);

        // Then
        verify(userService).deleteUser(payload.getId());
    }

    @Test
    @DisplayName("Test if consume method does nothing on null event type")
    public void testConsume_NullEventType() {
        // Given
        UserEventPayload payload = new UserEventPayload();
        payload.setUserEventType(null);
        payload.setId(1L);
        payload.setUsername("username");

        // When
        userEventListener.consume(payload);

        // Then
        verify(userService, never()).addUser(any(User.class));
        verify(userService, never()).updateUser(any(User.class));
        verify(userService, never()).deleteUser(any());
    }
}

