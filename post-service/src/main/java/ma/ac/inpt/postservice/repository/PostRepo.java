package ma.ac.inpt.postservice.repository;

import ma.ac.inpt.postservice.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PostRepo extends MongoRepository<Post, String> {

    // Retrieve a list of posts for a given username, ordered by createdAt in descending order
    List<Post> findByUsernameOrderByCreatedAtDesc(String username);

    // Retrieve a list of posts for the given list of IDs, ordered by createdAt in descending order
    List<Post> findBy_idInOrderByCreatedAtDesc(List<String> ids);



}