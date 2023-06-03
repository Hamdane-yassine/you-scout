package ma.ac.inpt.commentservice.repository;

import ma.ac.inpt.commentservice.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}
