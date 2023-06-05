package ma.ac.inpt.postservice.postMessaging;

import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.payload.PostEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;

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
        post.set_id("post-id");
        post.setUsername("user1");
        post.setLikes(new ArrayList<>());

        PostEvent expectedPostEvent = PostEvent.builder()
                .eventType(PostEventType.CREATED)
                .id(post.get_id())
                .username(post.getUsername())
                .accessToken("access")
                .build();

        // Act
        postEventSender.sendPostCreated(post, "access");

        // Assert
        verify(kafkaTemplate, times(1)).send("post", expectedPostEvent);
    }
}
