package ma.ac.inpt.postservice.controller;


import lombok.RequiredArgsConstructor;
import ma.ac.inpt.postservice.exception.UploadFileException;
import ma.ac.inpt.postservice.exception.VideoProcessingException;
import ma.ac.inpt.postservice.exception.VideoValidationException;
import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.payload.ApiResponse;
import ma.ac.inpt.postservice.payload.PostRequest;
import ma.ac.inpt.postservice.payload.RatingRequest;
import ma.ac.inpt.postservice.service.PostService;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
import ma.ac.inpt.postservice.service.media.MediaService;
import org.springframework.http.ResponseEntity;

//import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;
@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    private final MediaService mediaService;

    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest, @RequestParam("video") MultipartFile file){
        log.info("Received a request to create a post for video {}", postRequest.getVideo());


        try{
            // Create the post
            Post post = postService.createPost(postRequest, file);

            // Build the URI for the created post
            URI location = ServletUriComponentsBuilder
                    .fromCurrentContextPath().path("/posts/{id}")
                    .buildAndExpand(post.getId()).toUri();

            // Return the response with the created post's location
            return ResponseEntity
                    .created(location)
                    .body(new ApiResponse(true, "Post created successfully"));


        } catch (VideoValidationException e){
            return ResponseEntity.badRequest().body("Can't validate video");
        } catch (VideoProcessingException e){
            return ResponseEntity.badRequest().body("Can't process video");
        } catch (UploadFileException e){
            return ResponseEntity.internalServerError().body("Can't upload video");
        }
    }


    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable("id") String id, @AuthenticationPrincipal Principal user) {
        log.info("Received a delete request for post id {} from user {}", id, user.getName());
        // Delete the post
        postService.deletePost(id, user.getName());
    }

    @DeleteMapping("/posts/{id}/like")
    public void likePost(@PathVariable("id") String id, @AuthenticationPrincipal Principal user) {
        log.info("Received a post liking request for post id {} from user {}", id, user.getName());

        // Like the post
        postService.likePost(id, user.getName());
    }

    @DeleteMapping("/posts/{id}/removelike")
    public void removeLikePost(@PathVariable("id") String id, @AuthenticationPrincipal Principal user) {
        log.info("Received a remove post liking request for post id {} from user {}", id, user.getName());

        // Remove the like from the post
        postService.removeLikePost(id, user.getName());
    }

    @PostMapping("/posts/{id}/rate")
    public void ratePost(@PathVariable("id") String id, @RequestBody RatingRequest ratingRequest, @AuthenticationPrincipal Principal user) {
        log.info("Received a rating post request for post id {} from user {}", id, user.getName());

        // Rate the post
        postService.ratePost(id, ratingRequest, user.getName());
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
