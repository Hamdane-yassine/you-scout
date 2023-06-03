package ma.ac.inpt.postservice.postMessaging;


import lombok.RequiredArgsConstructor;
import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.payload.PostEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class PostEventSender {

    private final KafkaTemplate<String, PostEvent> kafkaTemplate;
    public void sendPostCreated(Post post) {
        log.info("sending post created event for post id {}", post.getId());
        sendPostChangedEvent(convertTo(post, PostEventType.CREATED));
    }

    public void sendPostUpdated(Post post) {
        log.info("sending post updated event for post {}", post.getId());
        sendPostChangedEvent(convertTo(post, PostEventType.UPDATED));
    }

    public void sendPostDeleted(Post post) {
        log.info("sending post deleted event for post {}", post.getId());
        sendPostChangedEvent(convertTo(post, PostEventType.DELETED));
    }

    private void sendPostChangedEvent(PostEvent payload) {

        kafkaTemplate.send("amigoscode",payload);

    }

    private PostEvent convertTo(Post post, PostEventType eventType) {
        return PostEvent
                .builder()
                .eventType(eventType)
                .id(post.getId())
                .username(post.getUsername())
                .createdAt(post.getCreatedAt())
                .comments(post.getCommentsNum())
                .likes(post.getLikes().size())
                .build();
    }
}