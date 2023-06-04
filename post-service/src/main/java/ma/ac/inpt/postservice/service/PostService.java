package ma.ac.inpt.postservice.service;


import lombok.RequiredArgsConstructor;
import ma.ac.inpt.postservice.exception.NotAllowedException;
import ma.ac.inpt.postservice.exception.ResourceNotFoundException;
import ma.ac.inpt.postservice.payload.RatingRequest;
import ma.ac.inpt.postservice.postMessaging.PostEventSender;
import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.payload.PostRequest;
import ma.ac.inpt.postservice.repository.PostRepo;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.postservice.service.media.MediaService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepo postRepository;
    private final PostEventSender postEventSender;
    private final MediaService mediaService;

    /**
     * Creates a new post.
     *
     * @param postRequest the post request object
     * @return the created post
     */
    public Post createPost(PostRequest postRequest, MultipartFile file) {
        log.info("creating post video url {}", postRequest.getVideo());

        // Create a new Post object using the data from the post request
        Post post = new Post(
                Instant.now(),
                postRequest.getUsername(),
                postRequest.getUserProfilePic(),
                postRequest.getCaption(),
                postRequest.getLikes().orElse(new ArrayList<>()),
                postRequest.getSkills().orElse(new ArrayList<>()),
                new HashMap<>()
        );
        String fileUrl = mediaService.uploadFile(file);
        post.setVideoUrl(fileUrl);
        // Save the post to the repository
        post = postRepository.save(post);

        // Send a post created event
        postEventSender.sendPostCreated(post);

        log.info("post {} is saved successfully for user {}", post.get_id(), post.getUsername());

        return post;
    }

    /**
     * Deletes a post with the given ID and username.
     *
     * @param postId   the ID of the post to delete
     * @param username the username of the user deleting the post
     */
    public void deletePost(String postId, String username) {
        log.info("deleting post {}", postId);

        // Find the post by ID
        postRepository.findById(postId).map(post -> {
            if (!post.getUsername().equals(username)) {
                log.warn("user {} is not allowed to delete post id {}", username, postId);
                throw new NotAllowedException(username, "post id " + postId, "delete");
            }

            mediaService.deleteFile(post.getVideoUrl());
            // Delete the post from the repository
            postRepository.delete(post);

            // Send a post deleted event
            postEventSender.sendPostDeleted(post);

            return post;
        }).orElseThrow(() -> {
            log.warn("Post not found id {}", postId);
            return new ResourceNotFoundException(postId);
        });
    }

    /**
     * Likes a post with the given ID by the specified username.
     *
     * @param postId   the ID of the post to like
     * @param username the username of the user liking the post
     */
    public void likePost(String postId, String username) {
        log.info("liking post {} by {}", postId, username);
        postRepository.findById(postId).map(post -> {
            post.getLikes().add(username);
            postRepository.save(post);
            return post;
        }).orElseThrow(() -> {
            log.warn("post not found id {}", postId);
            return new ResourceNotFoundException(postId);
        });
    }

    /**
     * Removes the like from a post with the given ID by the specified username.
     *
     * @param postId   the ID of the post to remove the like from
     * @param username the username of the user removing the like
     */
    public void removeLikePost(String postId, String username) {
        log.info("removing like from post {} by {}", postId, username);
        postRepository.findById(postId).map(post -> {
            if (!post.getLikes().contains(username)) {
                log.warn("user {} has not liked post {} to begin with", username, postId);
                throw new NotAllowedException(username, "post id " + postId, "remove like");
            }
            post.getLikes().remove(username);
            postRepository.save(post);
            return post;
        }).orElseThrow(() -> {
            log.warn("post not found id {}", postId);
            return new ResourceNotFoundException(postId);
        });
    }

    /**
     * Retrieves a list of posts by the given username, ordered by created date descending.
     *
     * @param username the username of the user
     * @return a list of posts
     */
    public List<Post> postsByUsername(String username) {
        return postRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    /**
     * Retrieves a list of posts with the given IDs, ordered by created date descending.
     *
     * @param ids the IDs of the posts
     * @return a list of posts
     */
    public List<Post> postsByIdIn(List<String> ids) {
        return postRepository.findByIdInOrderByCreatedAtDesc(ids);
    }

    /**
     * Rates a post with the given ID by the specified username.
     *
     * @param postId        the ID of the post to rate
     * @param ratingRequest the rating request object
     * @param username      the username of the user rating the post
     */
    public void ratePost(String postId, RatingRequest ratingRequest, String username) {
        log.info("rating post {} in service", postId);

        Map<String, Integer> rating = new HashMap<>();
        rating.put(username, ratingRequest.getRating());

        postRepository.findById(postId).map(post -> {
            post.getRates().put(username, ratingRequest.getRating());
            postRepository.save(post);
            return post;
        }).orElseThrow(() -> {
            log.warn("Post not found id {}", postId);
            return new ResourceNotFoundException(postId);
        });
    }

    /**
     * Updates the number of comments in a post with the given ID.
     *
     * @param postId  the ID of the post
     * @param num the new number of comments
     */
    public void updateCommentNum(String postId, int num) {
        log.info("updating number of comments in post {}", postId);

        postRepository.findById(postId).map(post -> {
            post.setCommentsNum(num);
            postRepository.save(post);
            return post;
        }).orElseThrow(() -> {
            log.warn("Post not found id {}", postId);
            return new ResourceNotFoundException(postId);
        });

    }
}
