package ma.ac.inpt.commentservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import ma.ac.inpt.commentservice.exceptions.CommentException;
import ma.ac.inpt.commentservice.model.Comment;
import ma.ac.inpt.commentservice.service.CommentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommentControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        CommentController commentController = new CommentController(commentService);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
        objectMapper = new ObjectMapper();
    }

    private Principal createPrincipal(String username) {
        return () -> username;
    }

    @Test
    void getAllCommentsForPost_WithValidPostIdAndQuery_ShouldReturnOkStatusAndComments() throws Exception {
        // Arrange
        String postId = "123";
        String query = "oldest";
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("1", "John", "First comment", null, postId, null, null));
        comments.add(new Comment("2", "Alice", "Second comment", null, postId, null, null));
        when(commentService.getAllCommentsForPost(postId, query)).thenReturn(comments);

        // Act
        MvcResult mvcResult = mockMvc.perform(get("/posts/{postId}/comments", postId)
                        .param("orderBy", query))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id").value(comments.get(0).getId()))
                .andExpect(jsonPath("$[0].author").value(comments.get(0).getAuthor()))
                .andExpect(jsonPath("$[0].body").value(comments.get(0).getBody()))
                .andExpect(jsonPath("$[1].id").value(comments.get(1).getId()))
                .andExpect(jsonPath("$[1].author").value(comments.get(1).getAuthor()))
                .andExpect(jsonPath("$[1].body").value(comments.get(1).getBody()))
                .andReturn();

        // Assert
        String responseContent = mvcResult.getResponse().getContentAsString();
        List<Comment> actualComments = objectMapper.readValue(responseContent, new TypeReference<>() {
        });
        ResponseEntity<List<Comment>> responseEntity = ResponseEntity.status(HttpStatus.OK).body(comments);
        List<Comment> expectedComments = responseEntity.getBody();
        assertEquals(expectedComments.get(0).getId(), actualComments.get(0).getId());
        assertEquals(expectedComments.get(1).getId(), actualComments.get(1).getId());

        assertEquals(expectedComments.get(0).getAuthor(), actualComments.get(0).getAuthor());
        assertEquals(expectedComments.get(1).getAuthor(), actualComments.get(1).getAuthor());
        assertEquals(expectedComments.get(0).getBody(), actualComments.get(0).getBody());
        assertEquals(expectedComments.get(1).getBody(), actualComments.get(1).getBody());
        verify(commentService).getAllCommentsForPost(postId, query);
    }

    @Test
    public void getComment_WithValidId_ShouldReturnComment() throws Exception {
        // Arrange
        String commentId = "123";
        Comment expectedComment = new Comment(commentId, "John", "Some comment", null, "456", null, null);
        when(commentService.getComment(commentId)).thenReturn(expectedComment);

        // Act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/comments/{id}", commentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Assert
        String responseContent = mvcResult.getResponse().getContentAsString();
        Comment actualComment = objectMapper.readValue(responseContent, Comment.class);
        Assertions.assertEquals(expectedComment.getId(), actualComment.getId());
        Assertions.assertEquals(expectedComment.getAuthor(), actualComment.getAuthor());
        Assertions.assertEquals(expectedComment.getBody(), actualComment.getBody());
        Mockito.verify(commentService).getComment(commentId);
    }


    @Test
    public void unlikeComment_WithValidIdAndUser_ShouldReturnSuccessMessage() throws Exception {
        // Arrange
        String commentId = "123";
        String userId = "john123";
        String successMessage = "Comment unliked successfully";
        when(commentService.unlikeComment(eq(commentId), eq(userId))).thenReturn(successMessage);

        // Act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/comments/{id}/unlike", commentId)
                        .principal(createPrincipal(userId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Assert
        String responseContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(successMessage, responseContent);
        Mockito.verify(commentService).unlikeComment(commentId, userId);
    }

    @Test
    public void unlikeComment_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        String commentId = "123";
        String userId = "john123";
        when(commentService.unlikeComment(eq(commentId), eq(userId))).thenThrow(new CommentException("Comment not found"));

        // Act
        mockMvc.perform(MockMvcRequestBuilders.put("/comments/{id}/unlike", commentId)
                        .principal(createPrincipal(userId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        // Assert
        Mockito.verify(commentService).unlikeComment(commentId, userId);
    }

    @Test
    public void unlikeComment_WithAlreadyLikedComment_ShouldReturnErrorMessage() throws Exception {
        // Arrange
        String commentId = "123";
        String userId = "john123";
        String errorMessage = "Comment already liked!";
        when(commentService.unlikeComment(eq(commentId), eq(userId))).thenReturn(errorMessage);

        // Act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/comments/{id}/unlike", commentId)
                        .principal(createPrincipal(userId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Assert
        String responseContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(errorMessage, responseContent);
        Mockito.verify(commentService).unlikeComment(commentId, userId);
    }

    @Test
    public void likeComment_WithValidIdAndUser_ShouldReturnSuccessMessage() throws Exception {
        // Arrange
        String commentId = "123";
        String userId = "john123";
        String successMessage = "Comment liked successfully";
        when(commentService.likeComment(eq(commentId), eq(userId))).thenReturn(successMessage);

        // Act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/comments/{id}/like", commentId)
                        .principal(createPrincipal(userId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Assert
        String responseContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(successMessage, responseContent);
        Mockito.verify(commentService).likeComment(commentId, userId);
    }

    @Test
    public void likeComment_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        String commentId = "123";
        String userId = "john123";
        when(commentService.likeComment(eq(commentId), eq(userId))).thenThrow(new CommentException("Comment not found"));

        // Act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/comments/{id}/like", commentId)
                        .principal(createPrincipal(userId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        // Assert
        String responseContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals("Comment not found", responseContent);
        Mockito.verify(commentService).likeComment(commentId, userId);
    }

    @Test
    public void likeComment_WithAlreadyLikedComment_ShouldReturnErrorMessage() throws Exception {
        // Arrange
        String commentId = "123";
        String userId = "john123";
        String errorMessage = "Comment already liked!";
        when(commentService.likeComment(eq(commentId), eq(userId))).thenReturn(errorMessage);

        // Act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/comments/{id}/like", commentId)
                        .principal(createPrincipal(userId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Assert
        String responseContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(errorMessage, responseContent);
        Mockito.verify(commentService).likeComment(commentId, userId);
    }

    @Test
    public void deleteComment_WithValidId_ShouldReturnSuccessMessage() throws Exception {
        // Arrange
        String commentId = "123";
        String successMessage = "Comment deleted!";
        when(commentService.deleteComment(eq(commentId))).thenReturn(successMessage);

        // Act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/comments/{id}", commentId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // Assert
        String responseContent = mvcResult.getResponse().getContentAsString();
        Assertions.assertEquals(successMessage, responseContent);
        Mockito.verify(commentService).deleteComment(commentId);
    }

}
