package ma.ac.inpt.service.messaging;

import ma.ac.inpt.postservice.messaging.PostEventSender;
import ma.ac.inpt.postservice.messaging.PostEventType;
import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.payload.PostEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

public class PostEventSenderTest {

    @Mock
    private KafkaTemplate<String, PostEvent> kafkaTemplate;

    private PostEventSender postEventSender;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        postEventSender = new PostEventSender(kafkaTemplate);
    }

    @Test
    public void sendPostCreated_ShouldSendPostEventToKafkaTemplate() {
        // Arrange
        Post post = new Post();
        post.setId("post-id");
        post.setUsername("user1");
        post.setCaption("Hello, world!");

        PostEvent expectedPostEvent = PostEvent.builder()
                .eventType(PostEventType.CREATED)
                .id(post.getId())
                .username(post.getUsername())
                .caption(post.getCaption())
                .build();

        // Act
        postEventSender.sendPostCreated(post);

        // Assert
        verify(kafkaTemplate, times(1)).send("amigoscode", expectedPostEvent);
    }
}
