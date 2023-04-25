package ma.ac.inpt.kafkaMessaging;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.models.Post;
import ma.ac.inpt.payload.PostEvent;
import ma.ac.inpt.service.FeedGenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostEventListener {

    private final FeedGenService feedGeneratorService;


    @KafkaListener(topics = "amigoscode", groupId = "post", containerFactory = "factory")
//    public void onMessage(Message<PostEvent> message) {
        public void consume(PostEvent message) {
        log.info(message.toString());
            PostEventType eventType = message.getEventType();
            switch (eventType) {
                case CREATED:
                    feedGeneratorService.addToFeed(convertTo(message));
                    break;
                case DELETED:
                    break;
            }
        }
//        PostEventType eventType = message.getPayload().getEventType();
//
//        log.info("received message to process post {} for user {} eventType {}",
//                message.getPayload().getId(),
//                message.getPayload().getUsername(),
//                eventType.name());
//
//        Acknowledgment acknowledgment =
//                message.getHeaders().get(KafkaHeaders.ACKNOWLEDGMENT, Acknowledgment.class);
//
//
//        switch (eventType) {
//            case CREATED:
//                feedGeneratorService.addToFeed(convertTo(message.getPayload()));
//                break;
//            case DELETED:
//                break;
//        }
//
//        if(acknowledgment != null) {
//            acknowledgment.acknowledge();
//        }


    private Post convertTo(PostEvent payload) {
        return Post
                .builder()
                .id(payload.getId())
                .createdAt(payload.getCreatedAt())
                .username(payload.getUsername())
                .build();
    }
}