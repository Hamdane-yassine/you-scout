package ma.ac.inpt.commentservice.repository;

import ma.ac.inpt.commentservice.model.Reply;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyRepository extends MongoRepository<Reply, String> {
    Optional<List<Reply>> findByRepliedTo(String commentId);
    Optional<List<Reply>> findByRepliedToOrderByTimestampAsc(String commentId);
    Optional<List<Reply>> findByRepliedToOrderByTimestampDesc(String commentId);
    void deleteRepliesByRepliedTo(String commentId);
}
