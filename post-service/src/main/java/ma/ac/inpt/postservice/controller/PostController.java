package ma.ac.inpt.postservice.controller;


import lombok.RequiredArgsConstructor;
import ma.ac.inpt.postservice.exception.UpdatingException;
import ma.ac.inpt.postservice.exception.UploadFileException;
import ma.ac.inpt.postservice.exception.VideoProcessingException;
import ma.ac.inpt.postservice.exception.VideoValidationException;
import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.payload.ApiResponse;
import ma.ac.inpt.postservice.payload.CompletePostRequest;
import ma.ac.inpt.postservice.payload.RatingRequest;
import ma.ac.inpt.postservice.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.security.Principal;
import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @PostMapping("/posts")
    public ResponseEntity<?> createPost( @RequestBody CompletePostRequest postRequest, @RequestHeader("Authorization") String authorizationHeader){

            String accessToken = authorizationHeader.replace("Bearer ", "");
            // Create the post
            Post post = postService.completePost(postRequest, accessToken);



            // Return the response with the created post's location
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "Post created successfully", post.get_id()));



    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadVideo( @RequestParam("video") MultipartFile file, Principal user){

        try{
            // Create the post
            String postId = postService.uploadVideo(file, user.getName());



            // Return the response with the created post's location
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse(true, "Video uploaded successfully", postId));


        } catch (VideoValidationException e){
            return ResponseEntity.badRequest().body("Can't validate video");
        } catch (VideoProcessingException e){
            return ResponseEntity.badRequest().body("Can't process video");
        } catch (UploadFileException e){
            return ResponseEntity.internalServerError().body("Can't upload video");
        }
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable("id") String id, @AuthenticationPrincipal Principal user, @RequestHeader("Authorization") String authorizationHeader) {
        log.info("Received a delete request for post id {} from user {}", id, user.getName());
        String accessToken = authorizationHeader.replace("Bearer ", "");

        // Delete the post
        postService.deletePost(id, user.getName(), accessToken);
    }

    @DeleteMapping("/posts/{id}/like")
    public void likePost(@PathVariable("id") String id, Principal user) {
        log.info("Received a post liking request for post id {} from user {}", id, user.getName());

        // Like the post
        postService.likePost(id, user.getName());
    }

    @DeleteMapping("/posts/{id}/removelike")
    public void removeLikePost(@PathVariable("id") String id, Principal user) {
        log.info("Received a remove post liking request for post id {} from user {}", id, user.getName());

        // Remove the like from the post
        postService.removeLikePost(id, user.getName());
    }

    @PostMapping("/posts/{id}/rate")
    public ResponseEntity<String> ratePost(@PathVariable("id") String id, @RequestBody RatingRequest ratingRequest, Principal user) {
        if(ratingRequest.getRating()>5 || ratingRequest.getRating()<0){
            return  ResponseEntity.ok("Not a valid rating");
        }
        log.info("Received a rating post request for post id {} from user {}", id, user.getName());

        // Rate the post
        try{

        postService.ratePost(id, ratingRequest, user.getName());
        return ResponseEntity.ok("Rating registered");
        } catch (UpdatingException e){
            return ResponseEntity.internalServerError().body("Rating failed");
        }
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
