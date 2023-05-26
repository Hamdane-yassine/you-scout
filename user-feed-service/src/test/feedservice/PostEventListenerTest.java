package feedservice;

import static org.junit.jupiter.api.Assertions.*;

import ma.ac.inpt.kafkaMessaging.PostEventListener;
import ma.ac.inpt.models.Post;
import ma.ac.inpt.payload.PostEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ma.ac.inpt.service.FeedGenService;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PostEventListenerTest {

    @Mock
    private FeedGenService feedGenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsume() {
        // Create a PostEvent object with the necessary properties for your test case
        PostEvent postEvent = new PostEvent();
        // Set the necessary properties of the postEvent object for your test case

        // Create an instance of PostEventListener
        PostEventListener postEventListener = new PostEventListener(feedGenService);

        // Call the consume method directly, passing the PostEvent object
        postEventListener.consume(postEvent);

        // Verify that the feedGenService.addToFeed method was called with the expected arguments
        verify(feedGenService, times(1)).addToFeed(any(Post.class));
    }
}
