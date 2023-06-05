package ma.ac.inpt.controller;

import ma.ac.inpt.config.SecurityTestConfig;
import ma.ac.inpt.models.Post;
import ma.ac.inpt.payload.PostEvent;
import ma.ac.inpt.payload.SlicedResult;
import ma.ac.inpt.postservice.FeedService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@WebMvcTest(FeedController.class)
@Import(SecurityTestConfig.class)
public class FeedControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FeedController feedController;

    @MockBean
    private FeedService feedService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(feedController).build();
    }

    @Test
    public void testGetFeed() throws Exception {
        String username = "testuser";
        Optional<String> pagingState = Optional.empty();
        String accessToken = "access_token";

        // Create a mock SlicedResult<Post> for the service response
        SlicedResult<Post> feedResult = SlicedResult.<Post>builder()
                .content(List.of(Post.builder()._id("the one").build()))
                .build();

        // Stub the feedService.getUserFeed() method to return the mock feedResult
        when(feedService.getUserFeed(username, pagingState, accessToken))
                .thenReturn(feedResult);

        // Expected JSON response
        String expectedJson = "{\"pagingState\":null,\"content\":[{\"id\":\"the one\",\"createdAt\":null,\"username\":null,\"userProfilePic\":null,\"updatedAt\":null,\"lastModifiedBy\":null,\"imageUrl\":null,\"caption\":null,\"commentsNum\":0}],\"last\":false}";

        mockMvc.perform(MockMvcRequestBuilders.get("/feed/{username}", username)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }
    private Post convertTo(PostEvent payload) {
        // Convert the PostEvent to a Post object
        return Post.builder()
                ._id(payload.getId())
                .createdAt(payload.getCreatedAt())
                .username(payload.getUsername())
                .build();
    }
}
