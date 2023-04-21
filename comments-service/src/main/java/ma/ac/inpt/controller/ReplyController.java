package ma.ac.inpt.controller;

import ma.ac.inpt.model.Reply;
import ma.ac.inpt.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ReplyController {
    private final ReplyService replyService;

    @Autowired
    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/comments/{commentId}/replies")
    public ResponseEntity<Reply> createReply(@PathVariable("commentId") String commentId, @RequestBody Reply reply) {
        Reply newReply = replyService.createReply(commentId, reply);
        return new ResponseEntity<>(newReply, HttpStatus.CREATED);
    }

    @GetMapping("/comments/{commentId}/replies")
    public ResponseEntity<List<Reply>> getRepliesForComment(@PathVariable("commentId") String commentId) {
        List<Reply> replies = replyService.getRepliesForComment(commentId);
        return new ResponseEntity<>(replies, HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}/replies/{replyId}")
    public ResponseEntity<String> deleteReplyForComment(@PathVariable("commentId") String commentId, @PathVariable("replyId") String replyId) {
        String message = replyService.deleteReplyForComment(commentId, replyId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
