package ma.ac.inpt.commentservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ma.ac.inpt.commentservice.model.User;
import ma.ac.inpt.commentservice.repository.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void testCreateUser() {
        // Arrange
        ObjectId objectId = new ObjectId();
        String id = objectId.toHexString();
        User user = new User("1", "John Doe", "profile.jpg");
        User newUser = new User(id, user.getUsername(), user.getProfileImg());

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Act
        User createdUser = userService.createUser(user);

        // Assert
        assertEquals(user.getUsername(), createdUser.getUsername());
        assertEquals(user.getProfileImg(), createdUser.getProfileImg());
        verify(userRepository).save(any(User.class));
    }
}