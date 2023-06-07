package ma.ac.inpt.commentservice.service;

import ma.ac.inpt.commentservice.exceptions.CommentException;
import ma.ac.inpt.commentservice.exceptions.ReplyException;
import ma.ac.inpt.commentservice.messaging.CommentEventSender;
import ma.ac.inpt.commentservice.model.Comment;
import ma.ac.inpt.commentservice.model.Reply;
import ma.ac.inpt.commentservice.repository.CommentRepository;
import ma.ac.inpt.commentservice.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReplyServiceTest {
    @Mock
    private ReplyRepository replyRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentEventSender commentEventSender;

    @InjectMocks
    private ReplyService replyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    // Test create Reply
    @Test
    void createReply_ValidCommentId_ReturnsNewReply() {
        // Arrange
        String commentId = "commentId";
        String user = "user";
        Reply reply = new Reply("1", user, "Reply body", commentId, LocalDateTime.now());

        List<String> replies = new ArrayList<>();
        List<String> likes = new ArrayList<>();

        Comment comment = new Comment("commentId", "User", "Comment body", replies, "postId", null, likes);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.findCommentsByPostId(comment.getPostId())).thenReturn(Optional.of(new ArrayList<>()));

        // Act
        Reply createdReply = replyService.createReply(commentId, reply, user);

        // Assert
        assertNotNull(createdReply);
        assertEquals(reply.getAuthor(), createdReply.getAuthor());
        assertEquals(reply.getBody(), createdReply.getBody());
        assertEquals(commentId, createdReply.getRepliedTo());
        assertNotNull(createdReply.getId());
        assertNotNull(createdReply.getRepliedTo());

        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).findCommentsByPostId(comment.getPostId());
        verify(commentEventSender, times(1)).sendCommentNum(comment.getPostId(), 1);
        verify(replyRepository, times(1)).save(any(Reply.class));
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
    @Test
    void createReply_InvalidCommentId_ThrowsCommentException() {
        // Arrange
        String commentId = "invalidCommentId";
        Reply reply = new Reply("1", "user", "Reply body", commentId, LocalDateTime.now());

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentException.class, () -> replyService.createReply(commentId, reply, "user"));

        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, never()).findCommentsByPostId(anyString());
        verify(commentEventSender, never()).sendCommentNum(anyString(), anyInt());
        verify(replyRepository, never()).save(any(Reply.class));
        verify(commentRepository, never()).save(any(Comment.class));
    }


    // Test Get Replies for comment
    @Test
    void getRepliesForComment_QueryTimestampAsc_ReturnsRepliesSortedByTimestampAsc() {
        // Arrange
        String commentId = "commentId";
        String query = "timestampAsc";
        List<Reply> expectedReplies = new ArrayList<>();
        expectedReplies.add(new Reply("1", "user1", "Reply 1", commentId, LocalDateTime.now()));
        expectedReplies.add(new Reply("2", "user2", "Reply 2", commentId, LocalDateTime.now().plusMinutes(1)));
        expectedReplies.add(new Reply("3", "user3", "Reply 3", commentId, LocalDateTime.now().plusMinutes(2)));

        when(replyRepository.findByRepliedToOrderByTimestampAsc(commentId)).thenReturn(Optional.of(expectedReplies));

        // Act
        List<Reply> actualReplies = replyService.getRepliesForComment(commentId, query);

        // Assert
        assertEquals(expectedReplies, actualReplies);

        verify(replyRepository, times(1)).findByRepliedToOrderByTimestampAsc(commentId);
        verify(replyRepository, never()).findByRepliedToOrderByTimestampDesc(commentId);
        verify(replyRepository, never()).findByRepliedTo(commentId);
    }
    @Test
    void getRepliesForComment_QueryTimestampDesc_ReturnsRepliesSortedByTimestampDesc() {
        // Arrange
        String commentId = "commentId";
        String query = "timestampDesc";
        List<Reply> expectedReplies = new ArrayList<>();
        expectedReplies.add(new Reply("3", "user3", "Reply 3", commentId, LocalDateTime.now().plusMinutes(2)));
        expectedReplies.add(new Reply("2", "user2", "Reply 2", commentId, LocalDateTime.now().plusMinutes(1)));
        expectedReplies.add(new Reply("1", "user1", "Reply 1", commentId, LocalDateTime.now()));

        when(replyRepository.findByRepliedToOrderByTimestampDesc(commentId)).thenReturn(Optional.of(expectedReplies));

        // Act
        List<Reply> actualReplies = replyService.getRepliesForComment(commentId, query);

        // Assert
        assertEquals(expectedReplies, actualReplies);

        verify(replyRepository, never()).findByRepliedToOrderByTimestampAsc(commentId);
        verify(replyRepository, times(1)).findByRepliedToOrderByTimestampDesc(commentId);
        verify(replyRepository, never()).findByRepliedTo(commentId);
    }
    @Test
    void getRepliesForComment_QueryOther_ReturnsRepliesWithoutSorting() {
        // Arrange
        String commentId = "commentId";
        String query = "other";
        List<Reply> expectedReplies = new ArrayList<>();
        expectedReplies.add(new Reply("1", "user1", "Reply 1", commentId, LocalDateTime.now()));
        expectedReplies.add(new Reply("2", "user2", "Reply 2", commentId, LocalDateTime.now().plusMinutes(1)));
        expectedReplies.add(new Reply("3", "user3", "Reply 3", commentId, LocalDateTime.now().plusMinutes(2)));

        when(replyRepository.findByRepliedTo(commentId)).thenReturn(Optional.of(expectedReplies));

        // Act
        List<Reply> actualReplies = replyService.getRepliesForComment(commentId, query);

        // Assert
        assertEquals(expectedReplies, actualReplies);

        verify(replyRepository, never()).findByRepliedToOrderByTimestampAsc(commentId);
        verify(replyRepository, never()).findByRepliedToOrderByTimestampDesc(commentId);
        verify(replyRepository, times(1)).findByRepliedTo(commentId);
    }
    @Test
    void getRepliesForComment_NoReplies_ThrowsReplyException() {
        // Arrange
        String commentId = "commentId";
        String query = "other";

        when(replyRepository.findByRepliedTo(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ReplyException.class, () -> replyService.getRepliesForComment(commentId, query));

        verify(replyRepository, never()).findByRepliedToOrderByTimestampAsc(commentId);
        verify(replyRepository, never()).findByRepliedToOrderByTimestampDesc(commentId);
        verify(replyRepository, times(1)).findByRepliedTo(commentId);
    }


    // Test Update Reply
    @Test
    void updateReplyForComment_ExistingCommentAndReply_ReturnsSuccessMessage() {
        // Arrange
        String commentId = "commentId";
        String replyId = "replyId";
        Reply newReply = new Reply(replyId, "user", "Updated reply body", commentId, LocalDateTime.now());
        Comment comment = new Comment(commentId, "User", "Comment body", new ArrayList<>(), "postId", null, new ArrayList<>());
        Reply reply = new Reply(replyId, "user", "Original reply body", commentId, LocalDateTime.now());

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(replyRepository.findById(replyId)).thenReturn(Optional.of(reply));
        when(replyRepository.save(reply)).thenReturn(reply);

        // Act
        String result = replyService.updateReplyForComment(commentId, replyId, newReply);

        // Assert
        assertEquals("Reply updated successfully!", result);
        assertEquals(newReply.getBody(), reply.getBody());

        verify(commentRepository, times(1)).findById(commentId);
        verify(replyRepository, times(1)).findById(replyId);
        verify(replyRepository, times(1)).save(reply);
    }
    @Test
    void updateReplyForComment_NonexistentComment_ThrowsCommentException() {
        // Arrange
        String commentId = "commentId";
        String replyId = "replyId";
        Reply newReply = new Reply(replyId, "user", "Updated reply body", commentId, LocalDateTime.now());

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentException.class, () -> replyService.updateReplyForComment(commentId, replyId, newReply));

        verify(commentRepository, times(1)).findById(commentId);
        verify(replyRepository, never()).findById(replyId);
        verify(replyRepository, never()).save(any(Reply.class));
    }
    @Test
    void updateReplyForComment_NonexistentReply_ThrowsReplyException() {
        // Arrange
        String commentId = "commentId";
        String replyId = "replyId";
        Reply newReply = new Reply(replyId, "user", "Updated reply body", commentId, LocalDateTime.now());
        Comment comment = new Comment(commentId, "User", "Comment body", new ArrayList<>(), "postId", null, new ArrayList<>());

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(replyRepository.findById(replyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ReplyException.class, () -> replyService.updateReplyForComment(commentId, replyId, newReply));

        verify(commentRepository, times(1)).findById(commentId);
        verify(replyRepository, times(1)).findById(replyId);
        verify(replyRepository, never()).save(any(Reply.class));
    }


    // Test delete Reply
    @Test
    void deleteReplyForComment_ExistingCommentAndReply_ReturnsSuccessMessage() {
        // Arrange
        String commentId = "commentId";
        String replyId = "replyId";
        Comment comment = new Comment(commentId, "User", "Comment body", new ArrayList<>(), "postId", null, new ArrayList<>());
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentRepository.findCommentsByPostId(comment.getPostId())).thenReturn(Optional.of(comments));
        doNothing().when(commentEventSender).sendCommentNum(comment.getPostId(), 0);
        doNothing().when(replyRepository).deleteById(replyId);

        // Act
        String result = replyService.deleteReplyForComment(commentId, replyId);

        // Assert
        assertEquals("Reply deleted!", result);

        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).findCommentsByPostId(comment.getPostId());
        verify(commentEventSender, times(1)).sendCommentNum(comment.getPostId(), 0);
        verify(replyRepository, times(1)).deleteById(replyId);
    }

    @Test
    void deleteReplyForComment_NonexistentComment_ThrowsCommentException() {
        // Arrange
        String commentId = "commentId";
        String replyId = "replyId";

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CommentException.class, () -> replyService.deleteReplyForComment(commentId, replyId));

        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, never()).findCommentsByPostId(anyString());
        verify(commentEventSender, never()).sendCommentNum(anyString(), anyInt());
        verify(replyRepository, never()).deleteById(anyString());
    }
}
