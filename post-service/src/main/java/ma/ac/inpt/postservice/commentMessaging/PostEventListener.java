package ma.ac.inpt.postservice.commentMessaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.postservice.payload.CommentNumEvent;
import ma.ac.inpt.postservice.service.PostService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PostEventListener {

    private final PostService postService;

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "post-service", containerFactory = "factory")
    // The method name can be changed to 'consume' to match the naming convention used in the example
    public void consume(CommentNumEvent message) {
        log.info("received the updated number of comments for post {}", message.getCommentNum());
        // Call the 'updateCommentNum' method of the 'PostService' to update the number of comments for the post
        postService.updateCommentNum(message.getId(), message.getCommentNum());
    }
}
