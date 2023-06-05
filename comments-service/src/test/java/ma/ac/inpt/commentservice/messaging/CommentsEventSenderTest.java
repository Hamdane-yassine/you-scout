package ma.ac.inpt.commentservice.messaging;

import ma.ac.inpt.commentservice.payload.CommentNumEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

class CommentsEventSenderTest {

    @Mock
    private KafkaTemplate<String, CommentNumEvent> kafkaTemplate;

    private CommentEventSender commentEventSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentEventSender = new CommentEventSender(kafkaTemplate);
    }

    @Test
    void sendCommentNum_ShouldSendMessageToKafka() {
        // Arrange
        String postId = "123";
        int commentsNum = 5;

        // Act
        commentEventSender.sendCommentNum(postId, commentsNum);

        // Assert
        Mockito.verify(kafkaTemplate).send("comment", new CommentNumEvent(postId, commentsNum));
    }
}
