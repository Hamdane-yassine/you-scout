package ma.ac.inpt.postservice.controller;
import ma.ac.inpt.postservice.exception.UpdatingException;
import ma.ac.inpt.postservice.exception.UploadFileException;
import ma.ac.inpt.postservice.exception.VideoProcessingException;
import ma.ac.inpt.postservice.exception.VideoValidationException;
import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.payload.ApiResponse;
import ma.ac.inpt.postservice.payload.CompletePostRequest;
import ma.ac.inpt.postservice.payload.PostRequest;
import ma.ac.inpt.postservice.payload.RatingRequest;
import ma.ac.inpt.postservice.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;
    @Mock
    private Principal principal;
//    @Test
//    public void testUploadVideo() throws Exception {
//        // Initialize mocks
//        MockitoAnnotations.initMocks(this);
//
//        // Mock input data
//        String postRequest = "{\"caption\":\"dsajlkfj\",\"userProfilePic\":\"fjkasdl\"}";
//        MultipartFile file = new MockMultipartFile("video", "test.mp4", "video/mp4", "test data".getBytes());
//        String accessToken = "your_access_token";
//        String username = "test_user";
//        // Mock the behavior of postService.createPost()
//        ApiResponse expectedApiResponse = new ApiResponse("Post created successfully", "postId", "videoUrl");
//        when(postService.createPost(any(PostRequest.class), eq(accessToken), eq(file), eq(username))).thenReturn(expectedApiResponse);
//        when(principal.getName()).thenReturn(username);
//
//
//        // Call the method
//        ResponseEntity<?> response = postController.uploadVideo(postRequest, file, principal, accessToken);
//        // Verify the response
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertEquals(expectedApiResponse, response.getBody());
//
//        // Verify that postService.createPost() was called with the correct arguments
//        verify(postService).createPost(any(PostRequest.class), eq(accessToken), eq(file), eq(username));
//    }


    @Test
    void deletePost_shouldCallPostServiceToDeletePost() {
        // Arrange
        String postId = "1";
        Principal user = () -> "username";
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
//        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        postController.deletePost(postId, user, "access");

        // Assert
        verify(postService, times(1)).deletePost(postId, user.getName(), "access");
    }

    @Test
    void likePost_shouldCallPostServiceToLikePost() {
        // Arrange
        String postId = "1";

        // Act
        postController.likePost(postId, () -> "ahmed");

        // Assert
        verify(postService, times(1)).likePost(postId, "ahmed");
    }

    @Test
    void removeLikePost_shouldCallPostServiceToRemoveLikeFromPost() {
        // Arrange
        String postId = "1";

        // Act
        postController.removeLikePost(postId, () -> "ahmed");

        // Assert
        verify(postService, times(1)).removeLikePost(postId, "ahmed");
    }

    @Test
    public void testRatePost() {
        // Create variables for post ID, RatingRequest, and Principal
        String postId = "123";
        RatingRequest ratingRequest = new RatingRequest();
        Principal principal = mock(Principal.class);

        // Mock the postService's ratePost method
        when(postService.ratePost(postId, ratingRequest, principal.getName())).thenReturn("Post rated successfully");

        // Call the ratePost method in the PostController
        ResponseEntity<String> response = postController.ratePost(postId, ratingRequest, principal);

        // Verify that the postService's ratePost method was called with the correct arguments
        verify(postService, times(1)).ratePost(postId, ratingRequest, principal.getName());

        // Verify the response status code and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Post rated successfully", response.getBody());
    }

    @Test
    void findUserPosts_shouldReturnListOfPosts() {
        // Arrange
        String username = "username";
        List<Post> posts = new ArrayList<>();
        when(postService.postsByUsername(username)).thenReturn(posts);

        // Act
        ResponseEntity<?> responseEntity = postController.findUserPosts(username);

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(posts);

        // Verify that the postService method was called
        verify(postService, times(1)).postsByUsername(username);
    }

    @Test
    void findPostsByIdIn_shouldReturnListOfPosts() {
        // Arrange
        List<String> ids = new ArrayList<>();
        List<Post> posts = new ArrayList<>();
        when(postService.postsByIdIn(ids)).thenReturn(posts);

        // Act
        ResponseEntity<?> responseEntity = postController.findPostsByIdIn(ids);

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(posts);

        // Verify that the postService method was called
        verify(postService, times(1)).postsByIdIn(ids);
    }

    // Add more tests for other methods in the PostController class

}


