package ma.ac.inpt.postservice.controller;
import ma.ac.inpt.postservice.exception.UpdatingException;
import ma.ac.inpt.postservice.exception.UploadFileException;
import ma.ac.inpt.postservice.exception.VideoProcessingException;
import ma.ac.inpt.postservice.exception.VideoValidationException;
import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.payload.CompletePostRequest;
import ma.ac.inpt.postservice.payload.RatingRequest;
import ma.ac.inpt.postservice.service.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    @Test
    void createPost_shouldReturnCreatedResponse() {
        // Arrange
        CompletePostRequest postRequest = new CompletePostRequest("username","urlImage","that's the stuff", new ArrayList<>(), new HashMap<>());
        Post createdPost = new Post("username","profilePic","that's the stuff");
        String expectedPostId = "1";
        when(postService.completePost(postRequest, "access")).thenReturn(createdPost);

        // Act
        ResponseEntity<?> responseEntity = postController.createPost(postRequest, "access");

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        // Assert other response properties if applicable

        // Verify that the postService method was called
        verify(postService, times(1)).completePost(postRequest, "access");
    }

    @Test
    void uploadVideo_shouldReturnCreatedResponse() throws VideoValidationException, VideoProcessingException, UploadFileException {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("video", "test.mp4", "video/mp4", new byte[]{});
        String user = "ayoub";
        String expectedPostId = "1";
        when(postService.uploadVideo(file, user)).thenReturn(expectedPostId);

        // Act
        ResponseEntity<?> responseEntity = postController.uploadVideo(file, () -> "ayoub");

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        // Assert other response properties if applicable

        // Verify that the postService method was called
        verify(postService, times(1)).uploadVideo(file, user);
    }

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
    void ratePost_withValidRating_shouldReturnOkResponse() throws UpdatingException {
        // Arrange
        String postId = "1";
        RatingRequest ratingRequest = new RatingRequest("dribbling",4);

        // Act
        ResponseEntity<String> responseEntity = postController.ratePost(postId, ratingRequest, () -> "ahmed");

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Rating registered");

        // Verify that the postService method was called
        verify(postService, times(1)).ratePost(postId, ratingRequest, "ahmed");
    }

    @Test
    void ratePost_withInvalidRating_shouldReturnOkResponse() throws UpdatingException {
        // Arrange
        String postId = "1";
        RatingRequest ratingRequest = new RatingRequest("dribbling",6);

        // Act
        ResponseEntity<String> responseEntity = postController.ratePost(postId, ratingRequest, () -> "ahmed");

        // Assert
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo("Not a valid rating");

        // Verify that the postService method was not called
        verify(postService, never()).ratePost(postId, ratingRequest, "ahmed");
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


