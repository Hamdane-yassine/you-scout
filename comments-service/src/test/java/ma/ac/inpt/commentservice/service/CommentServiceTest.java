package ma.ac.inpt.commentservice.service;

import ma.ac.inpt.commentservice.exceptions.CommentException;
import ma.ac.inpt.commentservice.exceptions.PostException;
import ma.ac.inpt.commentservice.exceptions.UserException;
import ma.ac.inpt.commentservice.messaging.PostEventSender;
import ma.ac.inpt.commentservice.model.Comment;
import ma.ac.inpt.commentservice.model.Post;
import ma.ac.inpt.commentservice.model.User;
import ma.ac.inpt.commentservice.repository.CommentRepository;
import ma.ac.inpt.commentservice.repository.PostRepository;
import ma.ac.inpt.commentservice.repository.ReplyRepository;
import ma.ac.inpt.commentservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ReplyRepository replyRepository;

    @Mock
    private PostEventSender postEventSender;

    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        commentService = new CommentService(commentRepository, userRepository,
                postRepository, replyRepository, postEventSender);
    }

    // Test createComment
    @Test
    public void testCreateComment_ValidData_Success() {
        // Arrange
        String postId = "1";
        List<String> comments = new ArrayList<>();
        String commentId = "3";
        String userId = "3";
        String body = "Test comment";

        Post post = new Post();
        post.setId(postId);
        post.setComments(comments);

        User user = new User();
        user.setId(userId);

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setPostId(postId);
        comment.setAuthor(user);
        comment.setBody(body);

        // Mock the behavior of postRepository.findById()
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Mock the behavior of userRepository.findById()
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Mock the behavior of commentRepository.save()
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        Comment createdComment = commentService.createComment(comment);

        // Assert
        assertNotNull(createdComment);
        assertEquals(postId, createdComment.getPostId());
        assertEquals(user, createdComment.getAuthor());
        assertEquals(body, createdComment.getBody());

        // Verify that postRepository.findById() was called
        verify(postRepository).findById(postId);

        // Verify that userRepository.findById() was called
        verify(userRepository).findById(userId);

        // Verify that commentRepository.save() was called
        verify(commentRepository).save(any(Comment.class));
    }
    @Test
    public void testCreateComment_UserNotFound() {
        // Arrange
        String postId = "1";
        String commentId = "3";
        String userId = "3";
        String body = "Test comment";

        Post post = new Post();
        post.setId(postId);

        User user = new User();

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setPostId(postId);
        comment.setAuthor(user);
        comment.setBody(body);

        // Mock the behavior of postRepository.findById()
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Mock the behavior of userRepository.findById()
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserException.class, () -> commentService.createComment(comment));

        // Verify that userRepository.findById() was called
        verify(userRepository).findById(userId);

        // Verify that postRepository.findById() was called
        verifyNoInteractions(postRepository);

        // Verify that commentRepository.save() was not called
        verifyNoInteractions(commentRepository);
    }

    // Test getAllCommentsForPost
    @Test
    public void testGetAllCommentsForPost_WithOldestQuery() {
        String postId = "post1";
        String userId = "1";
        String query = "oldest";

        Post post = new Post();
        post.setId(postId);

        User user = new User();
        user.setId(userId);

        List<Comment> expectedComments = new ArrayList<>();
        expectedComments.add(new Comment("comment1", null, null, null, null, LocalDateTime.now(), null));
        expectedComments.add(new Comment("comment2", null, null, null, null, LocalDateTime.now(), null));

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentRepository.findCommentsByPostIdOrderByTimestampAsc(postId)).thenReturn(Optional.of(expectedComments));

        List<Comment> result = commentService.getAllCommentsForPost(postId, query);

        assertEquals(expectedComments, result);
        verify(postRepository).findById(postId);
        verify(commentRepository).findCommentsByPostIdOrderByTimestampAsc(postId);
    }
    @Test
    public void testGetAllCommentsForPost_WithUnsupportedQuery() {
        String postId = "post1";
        String query = "invalid";

        Post post = new Post();
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        assertThrows(CommentException.class, () -> commentService.getAllCommentsForPost(postId, query));

        verify(postRepository).findById(postId);
        verify(commentRepository, never()).findCommentsByPostIdOrderByTimestampAsc(postId);
        verify(commentRepository, never()).findCommentsByPostIdOrderByTimestampDesc(postId);
        verify(commentRepository, never()).findCommentsByPostIdOrderByLikesDesc(postId);
    }

    // Test getComment
    @Test
    public void testGetComment_WithValidId() {
        String commentId = "comment1";
        Comment expectedComment = new Comment(commentId, null, null, null, null, null, null);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(expectedComment));

        Comment result = commentService.getComment(commentId);

        assertEquals(expectedComment, result);
        verify(commentRepository).findById(commentId);
    }
    @Test
    public void testGetComment_WithInvalidId() {
        String commentId = "invalid-comment";

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentException.class, () -> commentService.getComment(commentId));

        verify(commentRepository).findById(commentId);
    }

    // Test likeComment
    @Test
    public void testLikeComment_WithValidIdAndUser() {
        String commentId = "comment1";
        String userId = "user1";

        Comment comment = new Comment(commentId, null, null, null, null, null, new ArrayList<>());
        User user = new User(userId, "Name", "profile.png");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        String result = commentService.likeComment(commentId, user);

        assertEquals("Comment liked successfully", result);
        assertTrue(comment.getLikes().contains(userId));
        verify(commentRepository).findById(commentId);
        verify(commentRepository).save(comment);
    }
    @Test
    public void testLikeComment_WithInvalidId() {
        String commentId = "invalid-comment";
        User user = new User("user1", "Name", "profile.png");

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentException.class, () -> commentService.likeComment(commentId, user));

        verify(commentRepository).findById(commentId);
        verify(commentRepository, never()).save(any());
    }
    @Test
    public void testLikeComment_WithAlreadyLikedComment() {
        String commentId = "comment1";
        String userId = "user1";

        List<String> likes = new ArrayList<>();
        likes.add(userId);
        Comment comment = new Comment(commentId, null, null, null, null, null, likes);
        User user = new User(userId, "Name", "profile.png");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        String result = commentService.likeComment(commentId, user);

        assertEquals("Comment already liked!", result);
        assertEquals(1, comment.getLikes().size());
        verify(commentRepository).findById(commentId);
        verify(commentRepository, never()).save(any());
    }

    // Test unlikeComment
    @Test
    public void testUnlikeComment_WithValidIdAndUser() {
        String commentId = "comment1";
        String userId = "user1";

        List<String> likes = new ArrayList<>();
        likes.add(userId);
        Comment comment = new Comment(commentId, null, null, null, null, null, likes);
        User user = new User(userId, "Name", "profile.png");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        String result = commentService.unlikeComment(commentId, user);

        assertEquals("Comment unliked successfully", result);
        assertFalse(comment.getLikes().contains(userId));
        verify(commentRepository).findById(commentId);
        verify(commentRepository).save(comment);
    }

    @Test
    public void testUnlikeComment_WithInvalidId() {
        String commentId = "invalid-comment";
        User user = new User("user1", "Name", "profile.png");

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentException.class, () -> commentService.unlikeComment(commentId, user));

        verify(commentRepository).findById(commentId);
        verify(commentRepository, never()).save(any());
    }

    @Test
    public void testUnlikeComment_WithNotLikedComment() {
        String commentId = "comment1";
        String userId = "user1";

        Comment comment = new Comment(commentId, null, null, null, null, null, new ArrayList<>());
        User user = new User(userId, "Name", "profile.png");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        String result = commentService.unlikeComment(commentId, user);

        assertEquals("Comment was not liked!", result);
        assertEquals(0, comment.getLikes().size());
        verify(commentRepository).findById(commentId);
        verify(commentRepository, never()).save(any());
    }

    // Test updateComment
    @Test
    public void testUpdateComment_WithValidId() {
        String commentId = "comment1";
        String updatedBody = "Updated comment body";
        Comment comment = new Comment(commentId, null, "Old comment body", null, null, null, null);
        Comment newComment = new Comment(commentId, null, updatedBody, null, null, null, null);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        String result = commentService.updateComment(commentId, newComment);

        assertEquals("Comment updated successfully", result);
        assertEquals(updatedBody, comment.getBody());
        verify(commentRepository).findById(commentId);
        verify(commentRepository).save(comment);
    }

    @Test
    public void testUpdateComment_WithInvalidId() {
        String commentId = "invalid-comment";
        Comment newComment = new Comment(commentId, null, "Updated comment body", null, null, null, null);

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentException.class, () -> commentService.updateComment(commentId, newComment));

        verify(commentRepository).findById(commentId);
        verify(commentRepository, never()).save(any());
    }

    // Test deleteComment
    @Test
    public void testDeleteComment_WithValidId() {
        String commentId = "comment1";
        String postId = "post1";
        Comment comment = new Comment(commentId, null, null, null, postId, null, null);
        List<String> comments = new ArrayList<>();
        Post post = new Post(postId, comments);
        List<String> postComments = new ArrayList<>();
        postComments.add(commentId);
        post.setComments(postComments);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        String result = commentService.deleteComment(commentId);

        assertEquals("Comment deleted!", result);
        verify(commentRepository).findById(commentId);
        verify(commentRepository).deleteById(commentId);
        verify(postRepository).findById(postId);
        verify(postRepository).save(post);
        verify(replyRepository).deleteRepliesByRepliedTo(commentId);
    }

    @Test
    public void testDeleteComment_WithInvalidId() {
        String commentId = "invalid-comment";

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(CommentException.class, () -> commentService.deleteComment(commentId));

        verify(commentRepository).findById(commentId);
        verify(commentRepository, never()).deleteById(any());
        verify(postRepository, never()).findById(any());
        verify(postRepository, never()).save(any());
        verify(replyRepository, never()).deleteRepliesByRepliedTo(any());
    }

    @Test
    public void testDeleteComment_WithValidIdAndInvalidPostId() {
        String commentId = "comment1";
        String postId = "invalid-post";
        Comment comment = new Comment(commentId, null, null, null, postId, null, null);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(PostException.class, () -> commentService.deleteComment(commentId));

        verify(commentRepository).findById(commentId);
        verify(commentRepository, never()).deleteById(any());
        verify(postRepository).findById(postId);
        verify(postRepository, never()).save(any());
        verify(replyRepository, never()).deleteRepliesByRepliedTo(any());
    }
}
