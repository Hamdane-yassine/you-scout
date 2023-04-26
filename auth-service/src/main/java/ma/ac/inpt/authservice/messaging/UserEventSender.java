package ma.ac.inpt.authservice.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.payload.UserEventPayload;
import ma.ac.inpt.authservice.payload.UserEventType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserEventSender {

    @Value("${spring.kafka.topic.name}")
    private String topicName;

    private final KafkaTemplate<String, UserEventPayload> kafkaTemplate;

    public void sendUserCreated(User user) {
        log.info("sending user created event for user {}", user.getUsername());
        sendUserChangedEvent(convertTo(user, UserEventType.CREATED));
    }

    public void sendUserUpdated(User user) {
        log.info("sending user updated event for user {}", user.getUsername());
        sendUserChangedEvent(convertTo(user, UserEventType.UPDATED));
    }

    public void sendUserDeleted(User user) {
        log.info("sending user deleted event for user {}", user.getUsername());
        sendUserChangedEvent(convertTo(user, UserEventType.DELETED));
    }

    private void sendUserChangedEvent(UserEventPayload payload) {
        kafkaTemplate.send(topicName,payload);
        log.info("user event {} sent to topic {} for user {}", payload.getUserEventType().name(),topicName ,payload.getUsername());
    }

    private UserEventPayload convertTo(User user, UserEventType eventType) {
        UserEventPayload payload = new UserEventPayload();
        payload.setUserEventType(eventType);
        payload.setId(user.getId());
        payload.setUsername(user.getUsername());
        payload.setEmail(user.getEmail());
        return payload;
    }
}
