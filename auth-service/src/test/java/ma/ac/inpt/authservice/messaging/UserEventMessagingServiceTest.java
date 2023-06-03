package ma.ac.inpt.authservice.messaging;

import ma.ac.inpt.authservice.dto.UserEventPayload;
import ma.ac.inpt.authservice.dto.UserEventType;
import ma.ac.inpt.authservice.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserEventMessagingServiceTest {

    @Mock
    private KafkaTemplate<String, UserEventPayload> mockKafkaTemplate;

    @InjectMocks
    private UserEventMessagingServiceImpl userEventMessagingService;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Initializing test user
        testUser = User.builder().username("testUser").build();

        // Set the topic name field
        ReflectionTestUtils.setField(userEventMessagingService, "topicName", "test.topic");
    }

    @Test
    @DisplayName("Test Send User Created")
    void testSendUserCreated() {
        // When
        userEventMessagingService.sendUserCreated(testUser);

        // Then
        ArgumentCaptor<UserEventPayload> captor = ArgumentCaptor.forClass(UserEventPayload.class);
        verify(mockKafkaTemplate).send(eq("test.topic"), captor.capture());

        UserEventPayload sentPayload = captor.getValue();

        assertEquals(UserEventType.CREATED, sentPayload.getUserEventType());
        assertEquals(testUser.getId(), sentPayload.getId());
        assertEquals(testUser.getUsername(), sentPayload.getUsername());
    }

    @Test
    @DisplayName("Test Send User Updated")
    void testSendUserUpdated() {
        // When
        userEventMessagingService.sendUserUpdated(testUser);

        // Then
        ArgumentCaptor<UserEventPayload> captor = ArgumentCaptor.forClass(UserEventPayload.class);
        verify(mockKafkaTemplate).send(eq("test.topic"), captor.capture());

        UserEventPayload sentPayload = captor.getValue();

        assertEquals(UserEventType.UPDATED, sentPayload.getUserEventType());
        assertEquals(testUser.getId(), sentPayload.getId());
        assertEquals(testUser.getUsername(), sentPayload.getUsername());
    }

    @Test
    @DisplayName("Test Send User Deleted")
    void testSendUserDeleted() {
        // When
        userEventMessagingService.sendUserDeleted(testUser);

        // Then
        ArgumentCaptor<UserEventPayload> captor = ArgumentCaptor.forClass(UserEventPayload.class);
        verify(mockKafkaTemplate).send(eq("test.topic"), captor.capture());

        UserEventPayload sentPayload = captor.getValue();

        assertEquals(UserEventType.DELETED, sentPayload.getUserEventType());
        assertEquals(testUser.getId(), sentPayload.getId());
        assertEquals(testUser.getUsername(), sentPayload.getUsername());
    }
}

