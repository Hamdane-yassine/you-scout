package ma.ac.inpt.socialgraphservice.messaging;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.socialgraphservice.mapper.UserMapper;
import ma.ac.inpt.socialgraphservice.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;
import ma.ac.inpt.socialgraphservice.payload.*;
import ma.ac.inpt.socialgraphservice.model.User;

@Service
@RequiredArgsConstructor
public class UserEventListener {

    private final UserService userService;

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "social-graph-service",containerFactory = "factory")
    public void consume(UserEventPayload userEventPayload) {
        UserEventType eventType = userEventPayload.getUserEventType();
        User user = UserMapper.INSTANCE.userEventPayloadToUser(userEventPayload);
        switch (eventType) {
            case CREATED -> userService.addUser(user);
            case UPDATED -> userService.updateUser(user);
            case DELETED -> userService.deleteUser(user.getId());
        }
    }

}
