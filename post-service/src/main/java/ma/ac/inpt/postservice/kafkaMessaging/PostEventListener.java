package ma.ac.inpt.postservice.kafkaMessaging;

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

    @KafkaListener(topics = "comments", groupId = "post", containerFactory = "factory")
//    public void onMessage(Message<PostEvent> message) {
        public void consume(CommentNumEvent message) {
        log.info("received the updated number of comments for post {}", message.getCommentNum());
        postService.updateCommentNum(message.getId(),message.getCommentNum());
        }



}