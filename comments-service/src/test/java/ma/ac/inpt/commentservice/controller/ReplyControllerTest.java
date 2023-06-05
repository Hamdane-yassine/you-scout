package ma.ac.inpt.commentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ac.inpt.commentservice.config.SecurityTestConfig;
import ma.ac.inpt.commentservice.model.Reply;
import ma.ac.inpt.commentservice.model.User;
import ma.ac.inpt.commentservice.service.ReplyService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(ReplyController.class)
@Import(SecurityTestConfig.class)
public class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReplyService replyService;

    // Test createReply
    @Test
    public void testCreateReply() throws Exception {
        // Prepare the request body
        Reply reply = new Reply();
        reply.setAuthor(new User());
        reply.setBody("This is a reply.");

        // Mock the service method
        Reply createdReply = new Reply();
        createdReply.setId("1");
        createdReply.setAuthor(reply.getAuthor());
        createdReply.setBody(reply.getBody());
        createdReply.setRepliedTo("123");
        createdReply.setTimestamp(LocalDateTime.now());
        Mockito.when(replyService.createReply(Mockito.anyString(), Mockito.any(Reply.class)))
                .thenReturn(createdReply);

        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/comments/{commentId}/replies", "commentId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reply)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(createdReply.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author.username").value(createdReply.getAuthor().getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body").value(createdReply.getBody()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.repliedTo").value(createdReply.getRepliedTo()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists());
    }

    // Test getRepliesForComment
    @Test
    public void testGetRepliesForComment() throws Exception {
        // Prepare the mock data
        String commentId = "commentId";
        List<Reply> replies = new ArrayList<>();
        replies.add(new Reply());
        replies.add(new Reply());

        // Mock the service method
        Mockito.when(replyService.getRepliesForComment(commentId, ""))
                .thenReturn(replies);

        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/comments/{commentId}/replies", commentId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
    }

    // Test updateReplyForComment
    @Test
    public void testUpdateReplyForComment() throws Exception {
        // Prepare the request body
        Reply updatedReply = new Reply();
        updatedReply.setAuthor(new User());
        updatedReply.setBody("Updated reply.");

        // Mock the service method
        String message = "Reply updated successfully.";
        Mockito.when(replyService.updateReplyForComment(Mockito.anyString(), Mockito.anyString(), Mockito.any(Reply.class)))
                .thenReturn(message);

        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/comments/{commentId}/replies/{replyId}", "commentId", "replyId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedReply)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(message));
    }

    // Test deleteReplyForComment
    @Test
    public void testDeleteReplyForComment() throws Exception {
        // Mock the service method
        String message = "Reply deleted successfully.";
        Mockito.when(replyService.deleteReplyForComment(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(message);

        // Perform the request and assert the response
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/comments/{commentId}/replies/{replyId}", "commentId", "replyId"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(message));
    }
}
