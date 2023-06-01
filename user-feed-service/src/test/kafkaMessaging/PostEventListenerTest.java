package kafkaMessaging;
import ma.ac.inpt.kafkaMessaging.PostEventListener;
import ma.ac.inpt.models.Post;
import ma.ac.inpt.payload.PostEvent;
import ma.ac.inpt.service.FeedGenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PostEventListenerTest {

    @Mock
    private FeedGenService feedGeneratorService;

    @InjectMocks
    private PostEventListener postEventListener;

    @Captor
    private ArgumentCaptor<Post> postCaptor;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        postEventListener = new PostEventListener(feedGeneratorService);

    }

    @Test
    public void testConsume_CreatedEvent_AddsToFeed() {
        // Arrange
        PostEvent postEvent = PostEvent.builder().eventType(ma.ac.inpt.kafkaMessaging.PostEventType.CREATED).id("lol").createdAt(Instant.now()).username("ayoub").build();


        // Act
        postEventListener.consume(postEvent);

        // Assert
        verify(feedGeneratorService).addToFeed(postCaptor.capture());
        Post capturedPost = postCaptor.getValue();
        assertEquals("lol", capturedPost.getId());
        assertEquals("ayoub", capturedPost.getUsername());
    }

    @Test
    public void testConsume_DeletedEvent_DoesNothing() {
        // Arrange
        PostEvent postEvent = PostEvent.builder().eventType(ma.ac.inpt.kafkaMessaging.PostEventType.DELETED).build();

        // Act
        postEventListener.consume(postEvent);

    }
}
