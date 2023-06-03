package ma.ac.inpt.commentservice.service;

import ma.ac.inpt.commentservice.exceptions.PostException;
import ma.ac.inpt.commentservice.model.Post;
import ma.ac.inpt.commentservice.repository.CommentRepository;
import ma.ac.inpt.commentservice.repository.PostRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class PostServiceTest {
    private final PostRepository postRepository = Mockito.mock(PostRepository.class);
    private final CommentRepository commentRepository = Mockito.mock(CommentRepository.class);
    private final PostService postService = new PostService(postRepository, commentRepository);

    @Test
    public void createPost_ValidPost() {
        // Arrange
        String postId = "post1";
        List<String> comments = new ArrayList<>();
        Post post = new Post(postId, comments);

        when(postRepository.save(any(Post.class))).thenReturn(post);

        // Act
        Post createdPost = postService.createPost(post);

        // Assert
        assertNotNull(createdPost);
        assertEquals(postId, createdPost.getId());
        assertEquals(comments, createdPost.getComments());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void deletePost_ValidPostId() {
        // Arrange
        String postId = "post1";
        List<String> commentIds = new ArrayList<>();
        commentIds.add("comment1");
        commentIds.add("comment2");
        Post post = new Post(postId, commentIds);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        String result = postService.deletePost(postId);

        // Assert
        assertEquals("Post deleted!", result);
        verify(commentRepository, times(1)).deleteById("comment1");
        verify(commentRepository, times(1)).deleteById("comment2");
        verify(postRepository, times(1)).deleteById(postId);
    }

    @Test
    public void deletePost_InvalidPostId() {
        // Arrange
        String postId = "invalid_post";
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(PostException.class, () -> postService.deletePost(postId));
        verify(postRepository, times(1)).findById(postId);
        verifyNoInteractions(commentRepository);
    }
}
