package ma.ac.inpt.commentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ac.inpt.commentservice.model.Comment;
import ma.ac.inpt.commentservice.model.User;
import ma.ac.inpt.commentservice.service.CommentService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(CommentController.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;


    // Helper method to convert an object to JSON string
    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Test createComment
    @Test
    public void testCreateComment() throws Exception {
        // Prepare the request body
        Comment comment = new Comment();
        comment.setAuthor(new User());
        comment.setBody("Test comment");
        comment.setReplies(new ArrayList<>());
        comment.setPostId("12345");
        comment.setTimestamp(null);
        comment.setLikes(new ArrayList<>());

        // Prepare the expected response
        Comment createdComment = new Comment();
        createdComment.setId("commentId");
        createdComment.setAuthor(comment.getAuthor());
        createdComment.setBody(comment.getBody());
        createdComment.setReplies(comment.getReplies());
        createdComment.setPostId(comment.getPostId());
        createdComment.setTimestamp(comment.getTimestamp());
        createdComment.setLikes(comment.getLikes());

        // Mock the commentService.createComment() method
        Mockito.when(commentService.createComment(Mockito.any(Comment.class))).thenReturn(createdComment);

        // Perform the POST request
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(comment)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is("commentId")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body", Matchers.is("Test comment")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.replies", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.postId", Matchers.is("12345")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.likes", Matchers.notNullValue()));

        // Verify that commentService.createComment() was called with the expected comment
        Mockito.verify(commentService, Mockito.times(1)).createComment(Mockito.any(Comment.class));
    }

    // Test getAllCommentsForPost
    @Test
    public void testGetAllCommentsForPost() throws Exception {
        String postId = "12345";
        String orderBy = "timestamp";

        // Prepare the comments list
        List<Comment> comments = new ArrayList<>();
        Comment comment1 = new Comment();
        comment1.setId("commentId1");
        comment1.setAuthor(new User());
        comment1.setBody("Test comment 1");
        comment1.setReplies(new ArrayList<>());
        comment1.setPostId(postId);
        comment1.setTimestamp(LocalDateTime.now());
        comment1.setLikes(new ArrayList<>());
        comments.add(comment1);

        Comment comment2 = new Comment();
        comment2.setId("commentId2");
        comment2.setAuthor(new User());
        comment2.setBody("Test comment 2");
        comment2.setReplies(new ArrayList<>());
        comment2.setPostId(postId);
        comment2.setTimestamp(LocalDateTime.now());
        comment2.setLikes(new ArrayList<>());
        comments.add(comment2);

        // Mock the commentService.getAllCommentsForPost() method
        Mockito.when(commentService.getAllCommentsForPost(postId, orderBy)).thenReturn(comments);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/posts/{postId}/comments", postId)
                        .param("orderBy", orderBy)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("commentId1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].body").value("Test comment 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].replies").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].postId").value(postId))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].likes").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value("commentId2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].author").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].body").value("Test comment 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].replies").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].postId").value(postId))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].likes").exists());

        // Verify that commentService.getAllCommentsForPost() was called with the expected parameters
        Mockito.verify(commentService, Mockito.times(1)).getAllCommentsForPost(postId, orderBy);
    }

    // Test getLastCommentForPost
    @Test
    public void testGetLastCommentForPost() throws Exception {
        String postId = "12345";

        // Prepare the last comment
        Comment lastComment = new Comment();
        lastComment.setId("commentId");
        lastComment.setAuthor(new User());
        lastComment.setBody("Test comment");
        lastComment.setReplies(new ArrayList<>());
        lastComment.setPostId(postId);
        lastComment.setTimestamp(LocalDateTime.now());
        lastComment.setLikes(new ArrayList<>());

        // Mock the commentService.getLastCommentForPost() method
        Mockito.when(commentService.getLastCommentForPost(postId)).thenReturn(lastComment);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/posts/{postId}/lastComment", postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("commentId"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").value("Test comment"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.replies").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postId").value(postId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.likes").exists());

        // Verify that commentService.getLastCommentForPost() was called with the expected parameter
        Mockito.verify(commentService, Mockito.times(1)).getLastCommentForPost(postId);
    }

    // Test getComment
    @Test
    public void testGetComment() throws Exception {
        String commentId = "commentId";

        // Prepare the comment
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setAuthor(new User());
        comment.setBody("Test comment");
        comment.setReplies(new ArrayList<>());
        comment.setPostId("postId");
        comment.setTimestamp(LocalDateTime.now());
        comment.setLikes(new ArrayList<>());

        // Mock the commentService.getComment() method
        Mockito.when(commentService.getComment(commentId)).thenReturn(comment);

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/comments/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(commentId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").value("Test comment"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.replies").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.postId").value("postId"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.likes").exists());

        // Verify that commentService.getComment() was called with the expected parameter
        Mockito.verify(commentService, Mockito.times(1)).getComment(commentId);
    }

    // Test unlikeComment
    @Test
    public void testUnlikeComment() throws Exception {
        String commentId = "commentId";
        User user = new User();
        user.setId("userId");

        // Prepare the response message
        String message = "Comment unliked successfully";

        // Mock the commentService.unlikeComment() method
        Mockito.when(commentService.unlikeComment(commentId, user)).thenReturn(message);

        // Perform the PUT request
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/comments/{id}/unlike", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that commentService.unlikeComment() was called with the expected parameters
        Mockito.verify(commentService, Mockito.times(1)).unlikeComment(Mockito.eq(commentId), Mockito.any());
    }

    // Test likeComment
    @Test
    public void testLikeComment() throws Exception {
        String commentId = "commentId";
        User user = new User();
        user.setId("userId");

        // Prepare the response message
        String message = "Comment liked successfully";

        // Mock the commentService.likeComment() method
        Mockito.when(commentService.likeComment(commentId, user)).thenReturn(message);

        // Perform the PUT request
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/comments/{id}/like", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that commentService.likeComment() was called with the expected parameters
        Mockito.verify(commentService, Mockito.times(1)).likeComment(Mockito.eq(commentId), Mockito.any());
    }

    // Test updateComment
    @Test
    public void testUpdateComment() throws Exception {
        String commentId = "commentId";

        // Prepare the comment
        Comment updatedComment = new Comment();
        updatedComment.setId(commentId);
        updatedComment.setAuthor(new User());
        updatedComment.setBody("Updated comment");
        updatedComment.setReplies(new ArrayList<>());
        updatedComment.setPostId("postId");
        updatedComment.setTimestamp(null);
        updatedComment.setLikes(new ArrayList<>());

        // Prepare the response message
        String message = "Comment updated successfully";

        // Mock the commentService.updateComment() method
        Mockito.when(commentService.updateComment(Mockito.eq(commentId), Mockito.eq(updatedComment)))
                .thenReturn(message);

        // Perform the PUT request
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/comments/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedComment)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // Test deleteComment
    @Test
    public void testDeleteComment() throws Exception {
        String commentId = "commentId";

        // Prepare the response message
        String message = "Comment deleted successfully";

        // Mock the commentService.deleteComment() method
        Mockito.when(commentService.deleteComment(commentId)).thenReturn(message);

        // Perform the DELETE request
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/comments/{id}", commentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(message));

        // Verify that commentService.deleteComment() was called with the expected parameter
        Mockito.verify(commentService, Mockito.times(1)).deleteComment(commentId);
    }
}
