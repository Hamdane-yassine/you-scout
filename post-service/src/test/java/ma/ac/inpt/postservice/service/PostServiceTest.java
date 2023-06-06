package ma.ac.inpt.postservice.service;

import ma.ac.inpt.postservice.exception.NotAllowedException;
import ma.ac.inpt.postservice.exception.ResourceNotFoundException;
import ma.ac.inpt.postservice.exception.UploadFileException;
import ma.ac.inpt.postservice.payload.ApiResponse;
import ma.ac.inpt.postservice.payload.CompletePostRequest;
import ma.ac.inpt.postservice.postMessaging.PostEventSender;
import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.payload.PostRequest;
import ma.ac.inpt.postservice.repository.PostRepo;
import ma.ac.inpt.postservice.service.media.MediaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PostServiceTest {
    @Mock
    private PostRepo postRepository;

    @Mock
    private PostEventSender postEventSender;

    @Mock
    private MediaService mediaService;

    @Mock
    private MultipartFile multipartFile;

    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postService = new PostService(postRepository, postEventSender, mediaService);
    }

//    @Test
//    void createPost_ValidPostRequest_ReturnsCreatedPost() {
//        // Arrange
//        CompletePostRequest postRequest = new CompletePostRequest("id","profilePic","that's the stuff", new ArrayList<>(),new HashMap<>());
//        Post savedPost = new Post("username","profilePic","that's the stuff");
//        savedPost.set_id("id");
//        when(postRepository.save(any(Post.class))).thenReturn(savedPost);
//        when(postRepository.findById("id")).thenReturn(Optional.of(savedPost));
//
//        // Act
//        String createdPost = postService.completePost(postRequest, "access");
//
//        // Assert
//        assertEquals("Post id is completed!", createdPost);
//        verify(postRepository).save(any(Post.class));
//        verify(postEventSender).sendPostCreated(savedPost, "access");
//    }
//@Test
//public void testCreatePost() throws Exception {
//    // Initialize mocks
//    MockitoAnnotations.initMocks(this);
//
//    // Mock input data
//    PostRequest postRequest = new PostRequest();
//    postRequest.setCaption("Test Caption");
//    postRequest.setUserProfilePic("test_profile_pic");
//    MultipartFile file = new MockMultipartFile("video", "test.mp4", "video/mp4", "test data".getBytes());
//    String accessToken = "your_access_token";
//    String username = "test_user";
//
//    // Mock the behavior of mediaService.uploadFile()
//    String fileUrl = "https://example.com/test.mp4";
//    when(mediaService.uploadFile(eq(file))).thenReturn(fileUrl);
//
//    // Mock the behavior of postRepository.save()
//    Post savedPost = new Post();
//    savedPost.set_id("post_id");
//    savedPost.setVideoUrl(fileUrl);
//    when(postRepository.save(any(Post.class))).thenReturn(savedPost);
//
//    // Call the method
//    ApiResponse response = postService.createPost(postRequest, accessToken, file, username);
//
//    // Verify the response
//    assertEquals("Video uploaded and post created", response.getMessage());
//    assertEquals("post_id", response.getPostId());
//    assertEquals(fileUrl, response.getVideoUrl());
//
//    // Verify that mediaService.uploadFile() was called with the correct argument
//    verify(mediaService).uploadFile(eq(file));
//
//    // Verify that postRepository.save() was called with the correct argument
//    verify(postRepository).save(any(Post.class));
//
//    // Verify that postEventSender.sendPostCreated() was called with the correct arguments
//    verify(postEventSender).sendPostCreated(eq(savedPost), eq(accessToken));
//}
    @Test
    void deletePost_ExistingPostAndMatchingUsername_DeletesPost() {
        // Arrange
        String postId = "post-id";
        String username = "user1";
        Post post = new Post();
        post.set_id(postId);
        post.setUsername(username);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        assertDoesNotThrow(() -> postService.deletePost(postId, username, "access"));

        // Assert
        verify(postRepository).findById(postId);
        verify(postRepository).delete(post);
        verify(postEventSender).sendPostDeleted(post, "access");
    }

    @Test
    void deletePost_ExistingPostAndMismatchingUsername_ThrowsNotAllowedException() {
        // Arrange
        String postId = "post-id";
        String username = "user1";
        Post post = new Post();
        post.set_id(postId);
        post.setUsername("user2");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act & Assert
       assertThrows(NotAllowedException.class,
                () -> postService.deletePost(postId, username, "access"));


        verify(postRepository).findById(postId);
        verify(postRepository, never()).delete(any(Post.class));
        verify(postEventSender, never()).sendPostDeleted(any(Post.class), eq("access"));
    }

    @Test
    void deletePost_NonExistingPost_ThrowsResourceNotFoundException() {
        // Arrange
        String postId = "non-existing-post";

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> postService.deletePost(postId, "user1", "access"));

        verify(postRepository).findById(postId);
        verify(postRepository, never()).delete(any(Post.class));
        verify(postEventSender, never()).sendPostDeleted(any(Post.class), eq("access"));
    }

    @Test
    void likePost_ExistingPost_AddsUsernameToLikes() {
        // Arrange
        String postId = "post-id";
        String username = "user1";
        Post post = new Post();
        post.set_id(postId);
        post.setLikes(new ArrayList<>());

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        assertDoesNotThrow(() -> postService.likePost(postId, username));

        // Assert
        assertTrue(post.getLikes().contains(username));
        verify(postRepository).save(post);
    }

    @Test
    void likePost_NonExistingPost_ThrowsResourceNotFoundException() {
        // Arrange
        String postId = "non-existing-post";

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class,
                () -> postService.likePost(postId, "user1"));

        verify(postRepository).findById(postId);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void removeLikePost_ExistingPostAndLikedByUsername_RemovesUsernameFromLikes() {
        // Arrange
        String postId = "post-id";
        String username = "user1";
        Post post = new Post();
        post.set_id(postId);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(username);
        post.setLikes(arrayList);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        assertDoesNotThrow(() -> postService.removeLikePost(postId, username));

        // Assert
        assertFalse(post.getLikes().contains(username));
        verify(postRepository).save(post);
    }

    @Test
    void removeLikePost_ExistingPostAndNotLikedByUsername_ThrowsNotAllowedException() {
        // Arrange
        String postId = "post-id";
        String username = "user1";
        Post post = new Post();
        post.set_id(postId);
        post.setLikes(new ArrayList<>());

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act & Assert
        assertThrows(NotAllowedException.class,
                () -> postService.removeLikePost(postId, username));


        verify(postRepository).findById(postId);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void removeLikePost_NonExistingPost_ThrowsResourceNotFoundException() {
        // Arrange
        String postId = "non-existing-post";

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> postService.removeLikePost(postId, "user1"));

        verify(postRepository).findById(postId);
        verify(postRepository, never()).save(any(Post.class));
    }

    // Additional tests for other methods can be added similarly

}
