package ma.ac.inpt.postservice.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.payload.ApiResponse;
import ma.ac.inpt.postservice.payload.PostRequest;
import ma.ac.inpt.postservice.payload.RatingRequest;
import ma.ac.inpt.postservice.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @PostMapping("/create")
    public ResponseEntity<?> uploadVideo(@RequestPart("post") String postRequest, @RequestPart("video") MultipartFile file, Principal user, @RequestHeader("Authorization") String accessToken) {
        // Convert userStr to User object
        ObjectMapper mapper = new ObjectMapper();
        PostRequest post;
        try {
            post = mapper.readValue(postRequest, PostRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid user data", HttpStatus.BAD_REQUEST);
        }

        ApiResponse message = postService.createPost(post, accessToken, file, user.getName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(message);

    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") String id, Principal user, @RequestHeader("Authorization") String accessToken) {
        log.info("Received a delete request for post id {} from user {}", id, user.getName());

        // Delete the post
        postService.deletePost(id, user.getName(), accessToken);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/posts/{id}/like")
    public ResponseEntity<?> likePost(@PathVariable("id") String id, Principal user) {

        log.info("Received a post liking request for post id {} from user {}", id, user.getName());

        // Like the post
        String message = postService.likePost(id, user.getName());

        return ResponseEntity.ok(message);
    }

    @PostMapping("/posts/{id}/rate")
    public ResponseEntity<String> ratePost(@PathVariable("id") String id, @RequestBody RatingRequest ratingRequest, Principal user) {

        log.info("Received a rating post request for post id {} from user {}", id, user.getName());

        // Rate the post
        String message = postService.ratePost(id, ratingRequest, user.getName());
        return ResponseEntity.ok(message);

    }

    @GetMapping("/posts/{username}")
    public ResponseEntity<?> findUserPosts(@PathVariable("username") String username) {
        log.info("Retrieving posts for user {}", username);

        // Get the posts for the specified user
        List<Post> posts = postService.postsByUsername(username);
        log.info("Found {} posts for user {}", posts.size(), username);

        // Return the posts as the response
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/posts/in")
    public ResponseEntity<?> findPostsByIdIn(@RequestBody List<String> ids) {
        log.info("Retrieving posts for {} ids", ids.size());

        // Get the posts with the specified ids
        List<Post> posts = postService.postsByIdIn(ids);
        log.info("Found {} posts", posts.size());

        // Return the posts as the response
        return ResponseEntity.ok(posts);
    }
}
