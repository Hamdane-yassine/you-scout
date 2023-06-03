
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
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public ReplyService(ReplyRepository replyRepository,
                        UserRepository userRepository,
                        CommentRepository commentRepository) {
        this.replyRepository = replyRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public Reply createReply(String commentId, Reply reply) {
        ObjectId objectId = new ObjectId();
        String ID = objectId.toHexString();

        Optional<User> providedUser = userRepository.findById(reply.getId());
        User user = providedUser.orElseThrow(() -> new UserException("User not found"));

        Reply newReply = new Reply(ID, user, reply.getBody(), commentId, LocalDateTime.now());

        Optional<Comment> providedComment = commentRepository.findById(commentId);
        Comment comment = providedComment.orElseThrow(() -> new CommentException("Comment not found"));

        // Save reply only if comment exists
        replyRepository.save(newReply);

        // Append the reply ID in the comment's replies' List
        List<String> newReplies = comment.getReplies();
        newReplies.add(ID);
        comment.setReplies(newReplies);
        commentRepository.save(comment);

        return newReply;
    }

    public List<Reply> getRepliesForComment(String commentId, String query) {
        if (query.equals("timestampAsc")){
            Optional<List<Reply>> providedReplies = replyRepository.findByRepliedToOrderByTimestampAsc(commentId);
            return providedReplies.orElseThrow(() -> new ReplyException("Replies not found"));
        }
        if (query.equals("timestampDesc")){
            Optional<List<Reply>> providedReplies = replyRepository.findByRepliedToOrderByTimestampDesc(commentId);
            return providedReplies.orElseThrow(() -> new ReplyException("Replies not found"));
        }
        Optional<List<Reply>> providedReplies = replyRepository.findByRepliedTo(commentId);
        return providedReplies.orElseThrow(() -> new ReplyException("Replies not found"));
    }

    public String updateReplyForComment(String commentId, String replyId, Reply newReply) {
        // Check the existence of the comment
        Optional<Comment> providedComment = commentRepository.findById(commentId);
        if (providedComment.isEmpty()) {
            throw new  CommentException("Comment not found");
        }

        // Update the body of the reply
        Optional<Reply> providedReply = replyRepository.findById(replyId);
        Reply reply = providedReply.orElseThrow(() -> new ReplyException("Reply not found"));
        reply.setBody(newReply.getBody());
        replyRepository.save(reply);
        return "Reply updated successfully!";
    }

    public String deleteReplyForComment(String commentId, String replyId) {
        // Check the existence of the comment
        Optional<Comment> providedComment = commentRepository.findById(commentId);
        Comment comment = providedComment.orElseThrow(() -> new CommentException("Comment not found"));

        // Delete the reply ID from the comment's replies' List
        List<String> newReplies = comment.getReplies();
        newReplies.remove(replyId);
        comment.setReplies(newReplies);
        commentRepository.save(comment);

        replyRepository.deleteById(replyId);
        return "Reply deleted!";
    }
}
