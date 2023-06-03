package ma.ac.inpt.postservice.commentMessaging;

import ma.ac.inpt.postservice.payload.CommentNumEvent;
import ma.ac.inpt.postservice.service.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class CommentEventListenerTest {

    @Mock
    private PostService postService;

    private PostEventListener postEventListener;

    public CommentEventListenerTest() {
        MockitoAnnotations.openMocks(this);
        postEventListener = new PostEventListener(postService);
    }

    @Test
    public void testConsume() {
        // Create a test CommentNumEvent
        CommentNumEvent commentNumEvent = CommentNumEvent.builder().id("postId").commentNum(10).build();


        // Call the method being tested
        postEventListener.consume(commentNumEvent);

        // Verify that the postService's updateCommentNum method is called with the expected parameters
        verify(postService).updateCommentNum("postId", 10);
    }
}
