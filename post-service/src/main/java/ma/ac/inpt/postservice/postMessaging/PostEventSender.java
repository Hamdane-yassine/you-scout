package ma.ac.inpt.postservice.postMessaging;


import lombok.RequiredArgsConstructor;
import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.payload.PostEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class PostEventSender {

    private final KafkaTemplate<String, PostEvent> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String topicName;

    /**
     * Sends a post created event to Kafka.
     *
     * @param post The created post
     */
    public void sendPostCreated(Post post, String accessToken) {
        log.info("Sending post created event for post id {}", post.get_id());
        sendPostChangedEvent(convertTo(post, accessToken, PostEventType.CREATED));
    }


    /**
     * Sends a post deleted event to Kafka.
     *
     * @param post The deleted post
     */
    public void sendPostDeleted(Post post, String accessToken) {
        log.info("Sending post deleted event for post {}", post.get_id());
        sendPostChangedEvent(convertTo(post, accessToken, PostEventType.DELETED));
    }

    /**
     * Sends a post changed event to Kafka.
     *
     * @param payload The PostEvent payload to send
     */
    private void sendPostChangedEvent(PostEvent payload) {
        // Send the PostEvent payload to the "post" topic using the KafkaTemplate
        kafkaTemplate.send(topicName, payload);
    }

    /**
     * Converts a Post object to a PostEvent with the specified event type.
     *
     * @param post     The Post object to convert
     * @param eventType The event type of the PostEvent
     * @return The converted PostEvent
     */
    private PostEvent convertTo(Post post, String accessToken, PostEventType eventType) {
        // Convert a Post object to a PostEvent with the specified eventType
        return PostEvent.builder()
                .eventType(eventType)
                .id(post.get_id())
                .username(post.getUsername())
                .createdAt(post.getCreatedAt())
                .comments(post.getCommentsNum())
                .likes(post.getLikes().size())
                .accessToken(accessToken)
                .build();
    }
}
