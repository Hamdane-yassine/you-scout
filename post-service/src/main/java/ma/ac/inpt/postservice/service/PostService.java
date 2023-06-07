package ma.ac.inpt.postservice.service;


import lombok.RequiredArgsConstructor;
import ma.ac.inpt.postservice.exception.*;
import ma.ac.inpt.postservice.payload.ApiResponse;
import ma.ac.inpt.postservice.payload.PostRequest;
import ma.ac.inpt.postservice.payload.RatingRequest;
import ma.ac.inpt.postservice.postMessaging.PostEventSender;
import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.repository.PostRepo;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.postservice.service.media.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

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
    public ApiResponse createPost(PostRequest postRequest, String accessToken, MultipartFile file, String username) {
        String fileUrl;
        try {

            fileUrl = mediaService.uploadFile(file);

        } catch (UploadFileException e) {

            throw new UploadFileException("Can't upload video");
        }

        if (postRequest.getLikes() == null) {
            postRequest.setLikes(new ArrayList<>());
        }
        if (postRequest.getSkills() == null) {
            postRequest.setSkills(new HashMap<>());
        }
        Post post = new Post();
        post.setUsername(username);
        post.setVideoUrl(fileUrl);
        post.setCaption(postRequest.getCaption());
        post.setUserProfilePic(postRequest.getUserProfilePic());
        post.setLikes(new HashSet<>(postRequest.getLikes()));
        post.setSkills(postRequest.getSkills());
        post.setCommentsNum(0);

        Post createdPost = postRepository.save(post);

        postEventSender.sendPostCreated(createdPost, accessToken);

        return new ApiResponse("Video uploaded and post created", createdPost.get_id(), createdPost.getVideoUrl());
    }



    /**
     * Deletes a post with the given ID and username.
     *
     * @param postId   the ID of the post to delete
     * @param username the username of the user deleting the post
     */
    public void deletePost(String postId, String username, String accessToken) {
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
            postEventSender.sendPostDeleted(post, accessToken);

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
    public String likePost(String postId, String username) {

        log.info("liking post {} by {}", postId, username);
        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.warn("post not found id {}", postId);
            return new ResourceNotFoundException(postId);
        });
        post.getLikes().add(username);
        postRepository.save(post);
        return String.format("User %s liked the post %s", username, post.get_id());

    }

    /**
     * Removes the like from a post with the given ID by the specified username.
     *
     * @param postId   the ID of the post to remove the like from
     * @param username the username of the user removing the like
     */
    public String removeLikePost(String postId, String username) {

        log.info("removing like from post {} by {}", postId, username);

        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.warn("post not found id {}", postId);
            return new ResourceNotFoundException(postId);
        });

        if (!post.getLikes().contains(username)) {
            log.warn("user {} has not liked post {} to begin with", username, postId);
            throw new NotAllowedException(username, "post id " + postId, "remove like");
        }
        post.getLikes().remove(username);

        postRepository.save(post);

        return String.format("User %s removed like the post %s", username, post.get_id());
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
        return postRepository.findBy_idInOrderByCreatedAtDesc(ids);
    }

    /**
     * Rates a post with the given ID by the specified username.
     *
     * @param postId        the ID of the post to rate
     * @param ratingRequest the rating request object
     * @param username      the username of the user rating the post
     */
    public String ratePost(String postId, RatingRequest ratingRequest, String username) {
        log.info("rating post {} in service", postId);
        if (ratingRequest.getRating() > 5 || ratingRequest.getRating() < 0) {
            throw new InvalidRequestException("Not a valid rating");
        }
       try {
           Post post = postRepository.findById(postId).orElseThrow(() -> {
               log.warn("Post not found id {}", postId);
               return new ResourceNotFoundException(postId);
           });
           Map<String, Integer> ratings = post.getSkills().get(ratingRequest.getSkill());
           ratings.put(username, ratingRequest.getRating());
           post.getSkills().put(ratingRequest.getSkill(), ratings);
           postRepository.save(post);
           return String.format("User %s rated post %s with %d/5", username, post.get_id(), ratingRequest.getRating());
       } catch (UpdatingException e) {
            throw new UpdatingException("Rating wasn't registered");
        }
    }


    /**
     * Updates the number of comments in a post with the given ID.
     *
     * @param postId the ID of the post
     * @param num    the new number of comments
     */
    public void updateCommentNum(String postId, int num) {
        log.info("updating number of comments in post {}", postId);

        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.warn("Post not found id {}", postId);
            return new ResourceNotFoundException(postId);
        });
        ;
        post.setCommentsNum(num);
        Post updatedPost = postRepository.save(post);
        System.out.println(updatedPost);
    }
}
