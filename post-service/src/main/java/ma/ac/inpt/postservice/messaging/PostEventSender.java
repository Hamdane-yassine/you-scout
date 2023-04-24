package ma.ac.inpt.postservice.messaging;


import lombok.RequiredArgsConstructor;
import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.payload.PostEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class PostEventSender {

    private final KafkaTemplate<String, PostEvent> kafkaTemplate;
//    public PostEventSender(KafkaTemplate<String, PostEvent> kafkaTemplate){
//        log.info(kafkaTemplate.toString());
//    }
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

//        Message<PostEvent> message =
//                MessageBuilder
//                        .withPayload(payload)
//                        .setHeader(KafkaHeaders.MESSAGE_KEY, payload.getId())
//                        .build();
        kafkaTemplate.send("amigoscode",payload);

//        log.info("post event {} sent to topic {} for post {} and user {}",
//                message.getPayload().getEventType().name(),
//                kafkaTemplate.toString(),
//                message.getPayload().getId(),
//                message.getPayload().getUsername());
    }

    private PostEvent convertTo(Post post, PostEventType eventType) {
        return PostEvent
                .builder()
                .eventType(eventType)
                .id(post.getId())
                .username(post.getUsername())
                .caption(post.getCaption())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .lastModifiedBy(post.getLastModifiedBy())
                .imageUrl(post.getImageUrl())
                .build();
    }
}