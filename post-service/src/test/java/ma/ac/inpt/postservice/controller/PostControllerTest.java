package ma.ac.inpt.postservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.payload.ApiResponse;
import ma.ac.inpt.postservice.payload.PostRequest;
import ma.ac.inpt.postservice.service.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
public class PostControllerTest {

    @Mock
    private PostService postService;

    private PostController postController;
    public PostControllerTest() {
        MockitoAnnotations.openMocks(this);
        postController = new PostController(postService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));
    }

    @Test
    public void testCreatePost() {
        // Create a test PostRequest
        PostRequest postRequest = new PostRequest("id","username","urlImage","that's the stuff");

        // Mock the postService.createPost method
        Post post = new Post("id","username","urlImage","that's the stuff");
        post.setId("postId");
        when(postService.createPost(postRequest)).thenReturn(post);

        // Call the method being tested
        ResponseEntity<?> response = postController.createPost(postRequest);

        // Verify that the postService.createPost method is called
        verify(postService).createPost(postRequest);

        // Assert the response
        assert response.getStatusCode() == HttpStatus.CREATED;
        assert response.getBody() instanceof ApiResponse;
        ApiResponse apiResponse = (ApiResponse) response.getBody();
        assert apiResponse.getSuccess();
        assert apiResponse.getMessage().equals("Post created successfully");
    }


    @Test
    public void testDeletePost() {
        // Mock the Principal object
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testUser");

        // Call the method being tested
        postController.deletePost("postId", principal);

        // Verify that the postService.deletePost method is called with the expected parameters
        verify(postService).deletePost("postId", "testUser");
    }

    @Test
    public void testLikePost() {
        // Mock the Principal object
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testUser");

        // Call the method being tested
        postController.likePost("postId", principal);

        // Verify that the postService.likePost method is called with the expected parameters
        verify(postService).likePost("postId", "testUser");
    }

    @Test
    public void testRemoveLikePost() {
        // Mock the Principal object
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testUser");

        // Call the method being tested
        postController.removeLikePost("postId", principal);

        // Verify that the postService.removeLikePost method is called with the expected parameters
        verify(postService).removeLikePost("postId", "testUser");
    }

    @Test
    public void testFindUserPosts() {
        // Create a test list of posts
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());
        posts.add(new Post());

        // Mock the postService.postsByUsername method
        when(postService.postsByUsername("testUser")).thenReturn(posts);

        // Call the method being tested
        ResponseEntity<?> response = postController.findUserPosts("testUser");

        // Verify that the postService.postsByUsername method is called
        verify(postService).postsByUsername("testUser");

        // Assert the response
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() == posts;
    }

    @Test
    public void testFindPostsByIdIn() {
        // Create a test list of post IDs
        List<String> ids = new ArrayList<>();
        ids.add("postId1");
        ids.add("postId2");

        // Create a test list of posts
        List<Post> posts = new ArrayList<>();
        posts.add(new Post());
        posts.add(new Post());

        // Mock the postService.postsByIdIn method
        when(postService.postsByIdIn(ids)).thenReturn(posts);

        // Call the method being tested
        ResponseEntity<?> response = postController.findPostsByIdIn(ids);

        // Verify that the postService.postsByIdIn method is called
        verify(postService).postsByIdIn(ids);

        // Assert the response
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() == posts;
    }
}
