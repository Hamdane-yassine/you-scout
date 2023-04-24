package ma.ac.inpt.repository;

import ma.ac.inpt.model.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends MongoRepository<Comment, String> {
    Optional<List<Comment>> findCommentsByPostId(String replyId);
    Optional<List<Comment>> findCommentsByPostIdOrderByTimestampAsc(String replyId);
    Optional<List<Comment>> findCommentsByPostIdOrderByTimestampDesc(String replyId);
    Optional<List<Comment>> findCommentsByPostIdOrderByLikesDesc(String replyId);

}
