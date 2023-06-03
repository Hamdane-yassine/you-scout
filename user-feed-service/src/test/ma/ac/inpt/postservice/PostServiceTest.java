package ma.ac.inpt.postservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ma.ac.inpt.exceptions.UnableToGetPostsException;
import ma.ac.inpt.feignClient.PostClient;
import ma.ac.inpt.models.Post;

public class PostServiceTest {

    private PostClient postClient;
    private PostService postService;

    @BeforeEach
    public void setup() {
        postClient = mock(PostClient.class);
        postService = new PostService(postClient);
    }

    @Test
    public void testFindPostsIn_WithValidIds_ReturnsPostList() {
        // Mock the response from the PostClient
        List<Post> expectedPosts = Arrays.asList(Post.builder().commentsNum(1506).imageUrl("url").lastModifiedBy("yassine").updatedAt(Instant.now()).userProfilePic("image string").username("ayoub").createdAt(Instant.now()).id("1").caption("Post 1").build(), Post.builder().imageUrl("url").userProfilePic("image string").username("ayoub").createdAt(Instant.now()).id("2").caption("Post 2").build());
        ResponseEntity<List<Post>> response = new ResponseEntity<>(expectedPosts, HttpStatus.OK);
        when(postClient.findPostsByIdIn(anyList())).thenReturn(response);

        // Test the findPostsIn method
        List<String> ids = Arrays.asList("1", "2", "3");
        List<Post> result = postService.findPostsIn(ids);

        // Verify the method invocation
        verify(postClient, times(1)).findPostsByIdIn(eq(ids));

        // Assert the result
        assertEquals(expectedPosts, result);
    }

    @Test
    public void testFindPostsIn_WithInvalidIds_ThrowsUnableToGetPostsException() {
        // Mock the response from the PostClient
        ResponseEntity<List<Post>> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(postClient.findPostsByIdIn(anyList())).thenReturn(response);

        // Test the findPostsIn method with invalid ids
        List<String> ids = Arrays.asList("1", "2", "3");
        assertThrows(UnableToGetPostsException.class, () -> postService.findPostsIn(ids));

        // Verify the method invocation
        verify(postClient, times(1)).findPostsByIdIn(eq(ids));
    }
}
