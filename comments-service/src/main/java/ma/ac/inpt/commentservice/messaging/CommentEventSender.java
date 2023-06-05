package ma.ac.inpt.commentservice.messaging;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.commentservice.payload.CommentNumEvent;
import ma.ac.inpt.commentservice.model.Post;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class CommentEventSender {

    private final KafkaTemplate<String, CommentNumEvent> kafkaTemplate;
//    public PostEventSender(KafkaTemplate<String, PostEvent> kafkaTemplate){
//        log.info(kafkaTemplate.toString());
//    }
    public void sendCommentNum(Post post) {
        log.info("sending comment number event for post id {}", post.getId());

//        kafkaTemplate.send("comment",convertTo(post));
    }

//    private void sendPostChangedEvent(PostEvent payload) {
//
////        Message<PostEvent> message =
////                MessageBuilder
////                        .withPayload(payload)
////                        .setHeader(KafkaHeaders.MESSAGE_KEY, payload.getId())
////                        .build();
//        kafkaTemplate.send("amigoscode",payload);
//
////        log.info("post event {} sent to topic {} for post {} and user {}",
////                message.getPayload().getEventType().name(),
////                kafkaTemplate.toString(),
////                message.getPayload().getId(),
////                message.getPayload().getUsername());
//    }

    private CommentNumEvent convertTo(Post post) {
        return CommentNumEvent
                .builder()
                .id(post.getId())
                .commentNum(post.getComments().size())
                .build();
    }
}