package ma.ac.inpt.commentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ac.inpt.commentservice.model.User;
import ma.ac.inpt.commentservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;


    // Helper method to convert an object to JSON string
    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createUser_ValidUser() throws Exception {
        // Arrange
        User user = new User("1", "kaikimax", "profile.png");
        User createdUser = new User("1", "kaikimax", "profile.png");
        createdUser.setId("1234567890");

        when(userService.createUser(Mockito.any(User.class))).thenReturn(createdUser);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1234567890"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("kaikimax"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.profileImg").value("profile.png"));
    }
}
