package ma.ac.inpt.controller;

import ma.ac.inpt.model.Comment;
import ma.ac.inpt.model.User;
import ma.ac.inpt.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comments")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        Comment newComment = commentService.createComment(comment);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getAllCommentsForPost(@PathVariable("postId") String postId, @RequestParam(value = "orderBy", required = false, defaultValue = "") String query) {
        List<Comment> comments = commentService.getAllCommentsForPost(postId, query);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/posts/{postId}/lastComment")
    public ResponseEntity<Comment> getLastCommentForPost(@PathVariable("postId") String postId) {
        Comment lastComment = commentService.getLastCommentForPost(postId);
        return new ResponseEntity<>(lastComment, HttpStatus.OK);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable("id") String id) {
        Comment comment = commentService.getComment(id);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PutMapping("/comments/{id}/unlike")
    public ResponseEntity<String> unlikeComment(@PathVariable("id") String id, @RequestBody User user) {
        String message = commentService.unlikeComment(id, user);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/comments/{id}/like")
    public ResponseEntity<String> likeComment(@PathVariable("id") String id, @RequestBody User user) {
        String message = commentService.likeComment(id, user);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<String> updateComment(@PathVariable("id") String id, @RequestBody Comment comment) {
        String message = commentService.updateComment(id, comment);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") String id) {
        String message = commentService.deleteComment(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
