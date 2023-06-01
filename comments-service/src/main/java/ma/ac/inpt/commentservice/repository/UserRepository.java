package ma.ac.inpt.commentservice.repository;

import ma.ac.inpt.commentservice.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
