package ma.ac.inpt.commentservice.messaging;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.commentservice.payload.CommentNumEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class CommentEventSender {

    private final KafkaTemplate<String, CommentNumEvent> kafkaTemplate;
    @Value("${spring.kafka.topic.name}")
    private String topicName;

    public void sendCommentNum(String postId, int commentsNum) {
        log.info("sending comment number event for post id {}", postId);

        CommentNumEvent commentNumEvent = new CommentNumEvent(postId, commentsNum);
        kafkaTemplate.send(topicName, commentNumEvent);
    }
}