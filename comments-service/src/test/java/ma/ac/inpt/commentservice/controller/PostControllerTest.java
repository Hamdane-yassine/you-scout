package ma.ac.inpt.commentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ac.inpt.commentservice.config.SecurityTestConfig;
import ma.ac.inpt.commentservice.model.Post;
import ma.ac.inpt.commentservice.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@Import(SecurityTestConfig.class)
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @Test
    public void createPost_ValidPost() throws Exception {
        // Arrange
        Post post = new Post();
        post.setId("1");
        post.setComments(new ArrayList<>());

        when(postService.createPost(any(Post.class))).thenReturn(post);

        // Act
        ResultActions result = mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(post)));

        // Assert
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.comments").isArray())
                .andExpect(jsonPath("$.comments").isEmpty());

        verify(postService).createPost(any(Post.class));
    }

    @Test
    void deletePost_ExistingPostId() throws Exception {
        // Arrange
        String postId = "1";
        String message = "Post deleted";
        when(postService.deletePost(postId)).thenReturn(message);

        // Act
        ResultActions result = mockMvc.perform(delete("/posts/{postId}", postId)
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        result.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN + ";charset=UTF-8"))
                .andExpect(jsonPath("$").value(message));

        verify(postService).deletePost(postId);
    }
}
