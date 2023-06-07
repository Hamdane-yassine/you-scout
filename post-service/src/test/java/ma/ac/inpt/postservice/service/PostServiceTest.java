package ma.ac.inpt.postservice.service;

import ma.ac.inpt.postservice.exception.NotAllowedException;
import ma.ac.inpt.postservice.exception.ResourceNotFoundException;
import ma.ac.inpt.postservice.postMessaging.PostEventSender;
import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.repository.PostRepo;
import ma.ac.inpt.postservice.service.media.MediaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
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


    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postService = new PostService(postRepository, postEventSender, mediaService);
    }
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
        post.setLikes(new HashSet<>());

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
        HashSet<String> hashSet = new HashSet<>();
        hashSet.add(username);
        post.setLikes(hashSet);

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
        post.setLikes(new HashSet<>());

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
