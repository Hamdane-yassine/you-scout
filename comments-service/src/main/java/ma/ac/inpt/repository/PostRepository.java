package ma.ac.inpt.repository;

import ma.ac.inpt.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}
