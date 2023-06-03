package ma.ac.inpt.kafkaMessaging;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.models.Post;
import ma.ac.inpt.payload.PostEvent;
import ma.ac.inpt.postservice.FeedGenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostEventListener {

    private final FeedGenService feedGeneratorService;

    @KafkaListener(topics = "post", groupId = "posts", containerFactory = "factory")
    public void consume(PostEvent message) {
        log.info(message.toString());

        // Extract the event type from the message
        PostEventType eventType = message.getEventType();

        // Process the event based on its type
        switch (eventType) {
            case CREATED:
                // Convert the PostEvent to a Post object and add it to the feed
                feedGeneratorService.addToFeed(convertTo(message));
                break;
            case DELETED:
                // Handle the deletion event
                break;
        }
    }

    private Post convertTo(PostEvent payload) {
        // Convert the PostEvent to a Post object
        return Post.builder()
                .id(payload.getId())
                .createdAt(payload.getCreatedAt())
                .username(payload.getUsername())
                .build();
    }
}
