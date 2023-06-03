package ma.ac.inpt.socialgraphservice.messaging;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.socialgraphservice.mapper.UserMapper;
import ma.ac.inpt.socialgraphservice.model.User;
import ma.ac.inpt.socialgraphservice.payload.UserEventPayload;
import ma.ac.inpt.socialgraphservice.payload.UserEventType;
import ma.ac.inpt.socialgraphservice.service.UserService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Service class for consuming user events from Kafka.
 */
@Service
@RequiredArgsConstructor
public class UserEventListener {

    /**
     * The user service.
     */
    private final UserService userService;

    /**
     * Consumes user events from Kafka.
     *
     * @param userEventPayload the UserEventPayload object
     */
    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "social-graph-service", containerFactory = "factory")
    public void consume(UserEventPayload userEventPayload) {
        UserEventType eventType = userEventPayload.getUserEventType();
        if (eventType != null) {
            User user = UserMapper.INSTANCE.userEventPayloadToUser(userEventPayload);
            switch (eventType) {
                case CREATED -> userService.addUser(user);
                case UPDATED -> userService.updateUser(user);
                case DELETED -> userService.deleteUser(user.getId());
            }
        }
    }
}

