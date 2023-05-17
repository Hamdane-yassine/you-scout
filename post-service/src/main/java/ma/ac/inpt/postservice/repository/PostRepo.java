package ma.ac.inpt.postservice.repository;

import ma.ac.inpt.postservice.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface PostRepo extends MongoRepository<Post, String> {
    List<Post> findByUsernameOrderByCreatedAtDesc(String username);
    List<Post> findByIdInOrderByCreatedAtDesc(List<String> ids);
}