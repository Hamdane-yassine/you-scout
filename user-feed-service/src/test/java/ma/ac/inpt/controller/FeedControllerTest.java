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
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.HttpStatus;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
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
    public void testGetFeed() {
        // Create variables for username, pagingState, and authorization header
        String username = "john";
        Optional<String> pagingState = Optional.of("abc123");
        String authorizationHeader = "access_token";

        // Create a SlicedResult<Post> object for the mock response
        SlicedResult<Post> slicedResult = SlicedResult.<Post>builder()
                .content(List.of(Post.builder()._id("the one").build()))
                .build();
        // Set the necessary properties of the slicedResult

        // Mock the feedService's getUserFeed method
        when(feedService.getUserFeed(username, pagingState, "access_token")).thenReturn(slicedResult);

        // Call the getFeed method in the FeedController
        ResponseEntity<SlicedResult<Post>> response = feedController.getFeed(username, pagingState, authorizationHeader);

        // Verify that the feedService's getUserFeed method was called with the correct arguments
        verify(feedService, times(1)).getUserFeed(username, pagingState, "access_token");

        // Verify the response status code and body
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(slicedResult, response.getBody());
    }


}
