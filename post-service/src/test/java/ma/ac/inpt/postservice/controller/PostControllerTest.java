package ma.ac.inpt.postservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.payload.ApiResponse;
import ma.ac.inpt.postservice.payload.PostRequest;
import ma.ac.inpt.postservice.service.PostService;
import ma.ac.inpt.postservice.service.media.MediaService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
public class PostControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private MediaService mediaService;

    @Mock
    private MultipartFile multipartFile;
    private PostController postController;
    public PostControllerTest() {
        MockitoAnnotations.openMocks(this);
        postController = new PostController(postService, mediaService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request, response));
    }

    @Test
    public void testCreatePost() {
        // Create a test PostRequest
        PostRequest postRequest = new PostRequest("username","urlImage","video","that's the stuff", new ArrayList<>(), new ArrayList<>());

        // Mock the postService.createPost method
        Post post = new Post("username","profilePic","video","that's the stuff");
        post.set_id("postId");
        when(postService.createPost(postRequest, multipartFile)).thenReturn(post);

        // Call the method being tested
        ResponseEntity<?> response = postController.createPost(postRequest,multipartFile);

        // Verify that the postService.createPost method is called
        verify(postService).createPost(postRequest,multipartFile);

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
