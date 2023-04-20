package ma.ac.inpt.service;

import ma.ac.inpt.exceptions.CommentException;
import ma.ac.inpt.exceptions.ReplyException;
import ma.ac.inpt.exceptions.UserException;
import ma.ac.inpt.model.Comment;
import ma.ac.inpt.model.Reply;
import ma.ac.inpt.model.User;
import ma.ac.inpt.repository.CommentRepository;
import ma.ac.inpt.repository.ReplyRepository;
import ma.ac.inpt.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

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

        Reply newReply = new Reply(ID, user, reply.getBody(), commentId);
        replyRepository.save(newReply);

        Optional<Comment> providedComment = commentRepository.findById(commentId);
        Comment comment = providedComment.orElseThrow(() -> new CommentException("Comment not found"));

        // Append the reply ID in the comment's replies' List
        List<String> newReplies = comment.getReplies();
        newReplies.add(ID);
        comment.setReplies(newReplies);
        commentRepository.save(comment);

        return newReply;
    }

    public List<Reply> getRepliesForComment(String commentId) {
        Optional<List<Reply>> providedReplies = replyRepository.findByRepliedTo(commentId);
        List<Reply> replies = providedReplies.orElseThrow(() -> new ReplyException("Replies not found"));
        return replies;
    }
}
