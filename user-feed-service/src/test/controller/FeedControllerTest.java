package controller;

import config.SecurityConfig;
import lombok.RequiredArgsConstructor;
import ma.ac.inpt.FeedController;
import ma.ac.inpt.models.Post;
import ma.ac.inpt.payload.SlicedResult;
import ma.ac.inpt.service.FeedService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(FeedController.class)
@Import(SecurityConfig.class)
public class FeedControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedService feedService;

    @Test
    public void testGetFeed() throws Exception {
        String username = "testuser";
        Optional<String> pagingState = Optional.empty();
        SlicedResult<Post> feedResult = SlicedResult.<Post>builder().build();

        when(feedService.getUserFeed(username, pagingState)).thenReturn(feedResult);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/feed/{username}", username)
                        .param("ps", pagingState.orElse(""))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{}")); // replace with expected JSON response
    }
}
