package ma.ac.inpt.commentservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.ac.inpt.commentservice.model.Reply;
import ma.ac.inpt.commentservice.service.ReplyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReplyControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ReplyService replyService;

    @InjectMocks
    private ReplyController replyController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(replyController).build();
    }

    @Test
    public void testCreateReply() throws Exception {
        // Mock data
        String commentId = "comment123";
        Reply reply = new Reply();
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("username");

        // Mock service behavior
        Reply newReply = new Reply();
        when(replyService.createReply(eq(commentId), any(Reply.class), eq("username"))).thenReturn(newReply);

        // Perform the POST request
        mockMvc.perform(post("/comments/{commentId}/replies", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(reply))
                        .principal(principal))
                .andExpect(status().isCreated());

        // Verify the service method was called
        ArgumentCaptor<Reply> replyCaptor = ArgumentCaptor.forClass(Reply.class);
        verify(replyService).createReply(eq(commentId), replyCaptor.capture(), eq("username"));

        // Verify the captured argument
        Reply capturedReply = replyCaptor.getValue();
        assertThat(capturedReply);
    }


    private String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    public void testGetRepliesForComment() throws Exception {
        // Mock data
        String commentId = "comment123";
        String orderBy = "date";

        // Mock service behavior
        List<Reply> replies = new ArrayList<>();
        replies.add(new Reply());
        when(replyService.getRepliesForComment(commentId, orderBy)).thenReturn(replies);

        // Perform the GET request
        mockMvc.perform(get("/comments/{commentId}/replies", commentId)
                        .param("orderBy", orderBy))
                .andExpect(status().isOk());

        // Verify the service method was called
        verify(replyService).getRepliesForComment(commentId, orderBy);
    }

    @Test
    public void testUpdateReplyForComment() throws Exception {
        // Mock data
        String commentId = "comment123";
        String replyId = "reply456";
        Reply reply = new Reply();

        // Mock service behavior
        String message = "Reply updated successfully";
        when(replyService.updateReplyForComment(commentId, replyId, reply)).thenReturn(message);

        // Perform the PUT request
        mockMvc.perform(put("/comments/{commentId}/replies/{replyId}", commentId, replyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(reply)))
                .andExpect(status().isOk());

        // Verify the service method was called
        assertEquals(message, replyService.updateReplyForComment(commentId, replyId, reply));
    }

    @Test
    public void testDeleteReplyForComment() throws Exception {
        // Mock data
        String commentId = "comment123";
        String replyId = "reply456";

        // Mock service behavior
        String message = "Reply deleted successfully";
        when(replyService.deleteReplyForComment(commentId, replyId)).thenReturn(message);

        // Perform the DELETE request
        mockMvc.perform(delete("/comments/{commentId}/replies/{replyId}", commentId, replyId))
                .andExpect(status().isOk());

        // Verify the service method was called
        verify(replyService).deleteReplyForComment(commentId, replyId);
    }
}
