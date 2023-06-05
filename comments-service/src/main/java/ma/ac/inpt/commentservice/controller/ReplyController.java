package ma.ac.inpt.commentservice.controller;

import ma.ac.inpt.commentservice.model.Reply;
import ma.ac.inpt.commentservice.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping()
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
    public ResponseEntity<List<Reply>> getRepliesForComment(@PathVariable("commentId") String commentId, @RequestParam(value="orderBy", required = false, defaultValue = "") String query) {
        List<Reply> replies = replyService.getRepliesForComment(commentId, query);
        return new ResponseEntity<>(replies, HttpStatus.OK);
    }

    @PutMapping("/comments/{commentId}/replies/{replyId}")
    public ResponseEntity<String> updateReplyForComment(@PathVariable("commentId") String commentId, @PathVariable("replyId") String replyId, @RequestBody Reply reply) {
        String message = replyService.updateReplyForComment(commentId, replyId, reply);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}/replies/{replyId}")
    public ResponseEntity<String> deleteReplyForComment(@PathVariable("commentId") String commentId, @PathVariable("replyId") String replyId) {
        String message = replyService.deleteReplyForComment(commentId, replyId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
