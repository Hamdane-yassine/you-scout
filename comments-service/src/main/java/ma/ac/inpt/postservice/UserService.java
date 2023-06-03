package ma.ac.inpt.postservice;

import ma.ac.inpt.model.User;
import ma.ac.inpt.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        ObjectId objectId = new ObjectId();
        String ID = objectId.toHexString();

        User newUser = new User(ID, user.getUsername(), user.getProfileImg());
        userRepository.save(newUser);
        return newUser;
    }
}
