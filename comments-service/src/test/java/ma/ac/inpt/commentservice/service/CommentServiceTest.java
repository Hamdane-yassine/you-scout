package ma.ac.inpt.commentservice.service;

import ma.ac.inpt.commentservice.exceptions.CommentException;
import ma.ac.inpt.commentservice.messaging.CommentEventSender;
import ma.ac.inpt.commentservice.model.Comment;
import ma.ac.inpt.commentservice.repository.CommentRepository;
import ma.ac.inpt.commentservice.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ReplyRepository replyRepository;
    @Mock
    private CommentEventSender commentEventSender;

    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentService = new CommentService(commentRepository, replyRepository, commentEventSender);
    }

    @Test
    void createComment_ShouldSaveCommentAndReturnIt() {
        // Arrange
        String user = "John";
        String body = "This is a comment";
        String postId = "12345";
        Comment comment = new Comment(null, user, body, null, postId, null, null);
        List<Comment> comments = new ArrayList<>();
        when(commentRepository.findCommentsByPostId(postId)).thenReturn(Optional.of(comments));

        // Act
        Comment result = commentService.createComment(user, comment);

        // Assert
        verify(commentRepository).save(any(Comment.class));
        assertEquals(user, result.getAuthor());
        assertEquals(body, result.getBody());
        assertEquals(postId, result.getPostId());
        assertEquals(0, result.getLikes().size());
        verify(commentEventSender).sendCommentNum(postId, comments.size());
    }

    @Test
    void createComment_WithNoExistingComments_ShouldSetCommentNumToZero() {
        // Arrange
        String user = "John";
        String body = "This is a comment";
        String postId = "12345";
        Comment comment = new Comment(null, user, body, null, postId, null, null);
        when(commentRepository.findCommentsByPostId(postId)).thenReturn(Optional.empty());

        // Act
        Comment result = commentService.createComment(user, comment);

        // Assert
        verify(commentRepository).save(any(Comment.class));
        assertEquals(user, result.getAuthor());
        assertEquals(body, result.getBody());
        assertEquals(postId, result.getPostId());
        assertEquals(0, result.getLikes().size());
        verify(commentEventSender).sendCommentNum(postId, 0);
    }

    @Test
    void getAllCommentsForPost_WithOldestQuery_ShouldReturnCommentsOrderedByTimestampAsc() {
        // Arrange
        String postId = "12345";
        String query = "oldest";
        List<Comment> comments = new ArrayList<>();
        when(commentRepository.findCommentsByPostIdOrderByTimestampAsc(postId)).thenReturn(Optional.of(comments));

        // Act
        List<Comment> result = commentService.getAllCommentsForPost(postId, query);

        // Assert
        assertEquals(comments, result);
    }

    @Test
    void getAllCommentsForPost_WithRecentQuery_ShouldReturnCommentsOrderedByTimestampDesc() {
        // Arrange
        String postId = "12345";
        String query = "recent";
        List<Comment> comments = new ArrayList<>();
        when(commentRepository.findCommentsByPostIdOrderByTimestampDesc(postId)).thenReturn(Optional.of(comments));

        // Act
        List<Comment> result = commentService.getAllCommentsForPost(postId, query);

        // Assert
        assertEquals(comments, result);
    }

    @Test
    void getAllCommentsForPost_WithPopularityQuery_ShouldReturnCommentsOrderedByLikesDesc() {
        // Arrange
        String postId = "12345";
        String query = "popularity";
        List<Comment> comments = new ArrayList<>();
        when(commentRepository.findCommentsByPostIdOrderByLikesDesc(postId)).thenReturn(Optional.of(comments));

        // Act
        List<Comment> result = commentService.getAllCommentsForPost(postId, query);

        // Assert
        assertEquals(comments, result);
    }

    @Test
    void getAllCommentsForPost_WithInvalidQuery_ShouldThrowCommentException() {
        // Arrange
        String postId = "12345";
        String query = "invalid";
        when(commentRepository.findCommentsByPostIdOrderByLikesDesc(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentException.class, () -> commentService.getAllCommentsForPost(postId, query));
    }

    @Test
    void getAllCommentsForPost_WithInvalidPostId_ShouldThrowCommentException() {
        // Arrange
        String postId = "invalid";
        String query = "popularity";
        when(commentRepository.findCommentsByPostIdOrderByLikesDesc(postId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentException.class, () -> commentService.getAllCommentsForPost(postId, query));
    }

    @Test
    void getComment_WithExistingCommentId_ShouldReturnComment() {
        // Arrange
        String commentId = "12345";
        Comment comment = new Comment(commentId, "John", "This is a comment", null, "postId", null, null);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        Comment result = commentService.getComment(commentId);

        // Assert
        assertEquals(comment, result);
    }

    @Test
    void getComment_WithNonexistentCommentId_ShouldThrowCommentException() {
        // Arrange
        String commentId = "invalid";
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentException.class, () -> commentService.getComment(commentId));
    }

    @Test
    void likeComment_WithExistingCommentIdAndUserNotLiked_ShouldLikeCommentAndSave() {
        // Arrange
        String commentId = "12345";
        String user = "John";
        Comment comment = new Comment(commentId, "Author", "This is a comment", null, "postId", null, new ArrayList<>());
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        String result = commentService.likeComment(commentId, user);

        // Assert
        assertEquals("Comment liked successfully", result);
        assertTrue(comment.getLikes().contains(user));
        verify(commentRepository).save(comment);
    }

    @Test
    void likeComment_WithExistingCommentIdAndUserAlreadyLiked_ShouldNotLikeCommentOrSave() {
        // Arrange
        String commentId = "12345";
        String user = "John";
        List<String> likes = List.of(user, "Alice");
        Comment comment = new Comment(commentId, "Author", "This is a comment", null, "postId", null, likes);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        String result = commentService.likeComment(commentId, user);

        // Assert
        assertEquals("Comment already liked!", result);
        assertEquals(likes, comment.getLikes());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void likeComment_WithNonexistentCommentId_ShouldThrowCommentException() {
        // Arrange
        String commentId = "invalid";
        String user = "John";
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentException.class, () -> commentService.likeComment(commentId, user));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void unlikeComment_WithExistingCommentIdAndUserNotLiked_ShouldNotUnlikeCommentOrSave() {
        // Arrange
        String commentId = "12345";
        String user = "John";
        Comment comment = new Comment(commentId, "Author", "This is a comment", null, "postId", null, new ArrayList<>());
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        String result = commentService.unlikeComment(commentId, user);

        // Assert
        assertEquals("Comment was not liked!", result);
        assertFalse(comment.getLikes().contains(user));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void unlikeComment_WithNonexistentCommentId_ShouldThrowCommentException() {
        // Arrange
        String commentId = "invalid";
        String user = "John";
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentException.class, () -> commentService.unlikeComment(commentId, user));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void updateComment_WithExistingCommentId_ShouldUpdateCommentAndSave() {
        // Arrange
        String commentId = "12345";
        Comment existingComment = new Comment(commentId, "John", "This is the old comment", null, "postId", null, null);
        Comment updatedComment = new Comment(commentId, "John", "This is the new comment", null, "postId", null, null);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));

        // Act
        String result = commentService.updateComment(commentId, updatedComment);

        // Assert
        assertEquals("Comment updated successfully", result);
        assertEquals(updatedComment.getBody(), existingComment.getBody());
        verify(commentRepository).save(existingComment);
    }

    @Test
    void updateComment_WithNonexistentCommentId_ShouldThrowCommentException() {
        // Arrange
        String commentId = "invalid";
        Comment updatedComment = new Comment(commentId, "John", "This is the new comment", null, "postId", null, null);
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentException.class, () -> commentService.updateComment(commentId, updatedComment));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void deleteComment_WithExistingCommentId_ShouldDeleteCommentAndReplies() {
        // Arrange
        String commentId = "12345";
        Comment existingComment = new Comment(commentId, "John", "This is a comment", null, "postId", null, null);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));

        // Act
        String result = commentService.deleteComment(commentId);

        // Assert
        assertEquals("Comment deleted!", result);
        verify(replyRepository).deleteRepliesByRepliedTo(commentId);
        verify(commentRepository).deleteById(commentId);
    }

    @Test
    void deleteComment_WithNonexistentCommentId_ShouldThrowCommentException() {
        // Arrange
        String commentId = "invalid";
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentException.class, () -> commentService.deleteComment(commentId));
        verify(replyRepository, never()).deleteRepliesByRepliedTo(anyString());
        verify(commentRepository, never()).deleteById(anyString());
    }
}
