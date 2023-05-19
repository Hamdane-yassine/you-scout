package ma.ac.inpt.authservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.dto.UserEventPayload;
import ma.ac.inpt.authservice.dto.UserEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Implementation of UserEventMessagingService that sends user-related events to a Kafka topic.
 * Uses a KafkaTemplate to send UserEventPayload objects to the configured topic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventMessagingServiceImpl implements UserEventMessagingService {

    private final KafkaTemplate<String, UserEventPayload> kafkaTemplate;
    @Value("${spring.kafka.topic.name}")
    private String topicName;

    /**
     * Sends a user created event to the Kafka topic.
     *
     * @param user The user that was created.
     */
    @Override
    public void sendUserCreated(User user) {
        log.info("sending user created event for user {}", user.getUsername());
        sendUserChangedEvent(convertTo(user, UserEventType.CREATED));
    }

    /**
     * Sends a user updated event to the Kafka topic.
     *
     * @param user The user that was updated.
     */
    @Override
    public void sendUserUpdated(User user) {
        log.info("sending user updated event for user {}", user.getUsername());
        sendUserChangedEvent(convertTo(user, UserEventType.UPDATED));
    }

    /**
     * Sends a user deleted event to the Kafka topic.
     *
     * @param user The user that was deleted.
     */
    @Override
    public void sendUserDeleted(User user) {
        log.info("sending user deleted event for user {}", user.getUsername());
        sendUserChangedEvent(convertTo(user, UserEventType.DELETED));
    }

    /**
     * Sends a UserEventPayload to the Kafka topic using the KafkaTemplate.
     *
     * @param payload The UserEventPayload to send.
     */
    private void sendUserChangedEvent(UserEventPayload payload) {
        kafkaTemplate.send(topicName, payload);
        log.info("user event {} sent to topic {} for user {}", payload.getUserEventType().name(), topicName, payload.getUsername());
    }

    /**
     * Converts a User object to a UserEventPayload object with the given UserEventType.
     *
     * @param user      The User object to convert.
     * @param eventType The UserEventType for the converted UserEventPayload.
     * @return The converted UserEventPayload.
     */
    private UserEventPayload convertTo(User user, UserEventType eventType) {
        UserEventPayload payload = new UserEventPayload();
        payload.setUserEventType(eventType);
        payload.setId(user.getId());
        payload.setUsername(user.getUsername());
        return payload;
    }
}

