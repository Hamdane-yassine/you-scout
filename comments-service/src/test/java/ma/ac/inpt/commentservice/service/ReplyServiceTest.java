package ma.ac.inpt.commentservice.service;

import ma.ac.inpt.commentservice.exceptions.CommentException;
import ma.ac.inpt.commentservice.exceptions.ReplyException;
import ma.ac.inpt.commentservice.exceptions.UserException;
import ma.ac.inpt.commentservice.model.Comment;
import ma.ac.inpt.commentservice.model.Reply;
import ma.ac.inpt.commentservice.model.User;
import ma.ac.inpt.commentservice.repository.CommentRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReplyServiceTest {

    @Mock
    private ReplyRepository replyRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;

    private ReplyService replyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        replyService = new ReplyService(replyRepository, userRepository, commentRepository);
    }

    // Test createReply
    @Test
    public void testCreateReply_Success() {
        // Arrange
        String commentId = "1";
        String replyId = "3";
        String userId = "3";
        String replyBody = "This is a reply.";
        LocalDateTime timestamp = LocalDateTime.now();

        User user = new User(userId, "John Doe", "profile.png");
        Reply reply = new Reply(replyId, user, replyBody, commentId, timestamp);

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setReplies(new ArrayList<>());

        // Mock the behavior of userRepository.findById()
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Mock the behavior of commentRepository.findById()
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Mock the behavior of replyRepository.save()
        when(replyRepository.save(any(Reply.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Reply createdReply = replyService.createReply(commentId, reply);

        // Assert
        assertNotNull(createdReply);
        assertEquals(user, createdReply.getAuthor());
        assertEquals(replyBody, createdReply.getBody());
        assertEquals(commentId, createdReply.getRepliedTo());

        // Verify that userRepository.findById() was called
        verify(userRepository).findById(userId);

        // Verify that commentRepository.findById() was called
        verify(commentRepository).findById(commentId);

        // Verify that replyRepository.save() was called
        verify(replyRepository).save(any(Reply.class));

        // Verify that the reply ID was appended to the comment's replies' List
        assertEquals(1, comment.getReplies().size());

        // Verify that commentRepository.save() was called
        verify(commentRepository).save(comment);
    }
    @Test
    public void testCreateReply_UserNotFound() {
        // Arrange
        String commentId = "1";
        String replyId = "3";
        String userId = "3";
        String replyBody = "This is a reply.";

        Reply reply = new Reply(replyId, new User(userId, "John Doe", "profile.png"), replyBody, commentId, LocalDateTime.now());

        // Mock the behavior of userRepository.findById()
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UserException.class, () -> replyService.createReply(commentId, reply));

        // Verify that userRepository.findById() was called
        verify(userRepository).findById(userId);

        // Verify that commentRepository.findById() was not called
        verifyNoInteractions(commentRepository);

        // Verify that replyRepository.save() was not called
        verifyNoInteractions(replyRepository);
    }
    @Test
    public void testCreateReply_CommentNotFound() {
        // Arrange
        String commentId = "1";
        String replyId = "3";
        String userId = "3";
        String replyBody = "This is a reply.";

        Reply reply = new Reply(replyId, new User(userId, "John Doe", "profile.png"), replyBody, commentId, LocalDateTime.now());

        // Mock the behavior of userRepository.findById()
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User(userId, "John Doe", "profile.png")));

        // Mock the behavior of commentRepository.findById()
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(CommentException.class, () -> replyService.createReply(commentId, reply));

        // Verify that userRepository.findById() was called
        verify(userRepository).findById(userId);

        // Verify that commentRepository.findById() was called
        verify(commentRepository).findById(commentId);

        // Verify that replyRepository.save() was not called
        verifyNoInteractions(replyRepository);
    }

    // Test getRepliesForComment
    @Test
    public void testGetRepliesForComment_TimestampAsc_Success() {
        // Arrange
        String commentId = "1";
        String query = "timestampAsc";
        List<Reply> expectedReplies = new ArrayList<>();

        // Mock the behavior of replyRepository.findByRepliedToOrderByTimestampAsc()
        when(replyRepository.findByRepliedToOrderByTimestampAsc(commentId)).thenReturn(Optional.of(expectedReplies));

        // Act
        List<Reply> actualReplies = replyService.getRepliesForComment(commentId, query);

        // Assert
        assertEquals(expectedReplies, actualReplies);

        // Verify that replyRepository.findByRepliedToOrderByTimestampAsc() was called
        verify(replyRepository).findByRepliedToOrderByTimestampAsc(commentId);
    }
    @Test
    public void testGetRepliesForComment_TimestampAsc_RepliesNotFound() {
        // Arrange
        String commentId = "1";
        String query = "timestampAsc";

        // Mock the behavior of replyRepository.findByRepliedToOrderByTimestampAsc()
        when(replyRepository.findByRepliedToOrderByTimestampAsc(commentId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ReplyException.class, () -> replyService.getRepliesForComment(commentId, query));

        // Verify that replyRepository.findByRepliedToOrderByTimestampAsc() was called
        verify(replyRepository).findByRepliedToOrderByTimestampAsc(commentId);
    }
    @Test
    public void testGetRepliesForComment_TimestampDesc_Success() {
        // Arrange
        String commentId = "1";
        String query = "timestampDesc";
        List<Reply> expectedReplies = new ArrayList<>();

        // Mock the behavior of replyRepository.findByRepliedToOrderByTimestampDesc()
        when(replyRepository.findByRepliedToOrderByTimestampDesc(commentId)).thenReturn(Optional.of(expectedReplies));

        // Act
        List<Reply> actualReplies = replyService.getRepliesForComment(commentId, query);

        // Assert
        assertEquals(expectedReplies, actualReplies);

        // Verify that replyRepository.findByRepliedToOrderByTimestampDesc() was called
        verify(replyRepository).findByRepliedToOrderByTimestampDesc(commentId);
    }

    // Test updateReplyForComment
    @Test
    public void testUpdateReplyForComment_ReplyExists_Success() {
        // Arrange
        String commentId = "1";
        String replyId = "2";
        String updatedBody = "Updated reply body";
        Reply newReply = new Reply(replyId, null, updatedBody, commentId, null);

        Comment comment = new Comment();
        comment.setId(commentId);

        Reply existingReply = new Reply(replyId, null, "Original reply body", commentId, null);

        // Mock the behavior of commentRepository.findById()
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Mock the behavior of replyRepository.findById()
        when(replyRepository.findById(replyId)).thenReturn(Optional.of(existingReply));

        // Act
        String result = replyService.updateReplyForComment(commentId, replyId, newReply);

        // Assert
        assertEquals("Reply updated successfully!", result);
        assertEquals(updatedBody, existingReply.getBody());

        // Verify that commentRepository.findById() was called
        verify(commentRepository).findById(commentId);

        // Verify that replyRepository.findById() was called
        verify(replyRepository).findById(replyId);

        // Verify that replyRepository.save() was called
        verify(replyRepository).save(existingReply);
    }

    @Test
    public void testUpdateReplyForComment_CommentNotFound() {
        // Arrange
        String commentId = "1";
        String replyId = "2";
        Reply newReply = new Reply(replyId, null, "Updated reply body", commentId, null);

        // Mock the behavior of commentRepository.findById()
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(CommentException.class, () -> replyService.updateReplyForComment(commentId, replyId, newReply));

        // Verify that commentRepository.findById() was called
        verify(commentRepository).findById(commentId);

        // Verify that replyRepository.findById() was not called
        verifyNoInteractions(replyRepository);
    }

    @Test
    public void testUpdateReplyForComment_ReplyNotFound() {
        // Arrange
        String commentId = "1";
        String replyId = "2";
        Reply newReply = new Reply(replyId, null, "Updated reply body", commentId, null);

        Comment comment = new Comment();
        comment.setId(commentId);

        // Mock the behavior of commentRepository.findById()
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Mock the behavior of replyRepository.findById()
        when(replyRepository.findById(replyId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ReplyException.class, () -> replyService.updateReplyForComment(commentId, replyId, newReply));

        // Verify that commentRepository.findById() was called
        verify(commentRepository).findById(commentId);

        // Verify that replyRepository.findById() was called
        verify(replyRepository).findById(replyId);

        // Verify that replyRepository.save() was not called
        verifyNoMoreInteractions(replyRepository);
    }

    // Test deleteReplyForComment
    @Test
    public void testDeleteReplyForComment_ReplyExists_Success() {
        // Arrange
        String commentId = "1";
        String replyId = "2";

        Comment comment = new Comment();
        comment.setId(commentId);
        List<String> replies = new ArrayList<>();
        replies.add(replyId);
        comment.setReplies(replies);

        // Mock the behavior of commentRepository.findById()
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        String result = replyService.deleteReplyForComment(commentId, replyId);

        // Assert
        assertEquals("Reply deleted!", result);
        assertFalse(comment.getReplies().contains(replyId));

        // Verify that commentRepository.findById() was called
        verify(commentRepository).findById(commentId);

        // Verify that commentRepository.save() was called
        verify(commentRepository).save(comment);

        // Verify that replyRepository.deleteById() was called
        verify(replyRepository).deleteById(replyId);
    }
    @Test
    public void testDeleteReplyForComment_CommentNotFound() {
        // Arrange
        String commentId = "1";
        String replyId = "2";

        // Mock the behavior of commentRepository.findById()
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(CommentException.class, () -> replyService.deleteReplyForComment(commentId, replyId));

        // Verify that commentRepository.findById() was called
        verify(commentRepository).findById(commentId);

        // Verify that replyRepository.deleteById() was not called
        verifyNoInteractions(replyRepository);
    }
}
