package ma.ac.inpt.postservice.service;


import lombok.RequiredArgsConstructor;
import ma.ac.inpt.postservice.exception.NotAllowedException;
import ma.ac.inpt.postservice.exception.ResourceNotFoundException;
import ma.ac.inpt.postservice.messaging.PostEventSender;
import ma.ac.inpt.postservice.model.Post;
import ma.ac.inpt.postservice.payload.PostRequest;
import ma.ac.inpt.postservice.repository.PostRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepo postRepository;

    private final PostEventSender postEventSender;

    public Post createPost(PostRequest postRequest) {
        log.info("creating post image url {}", postRequest.getImageUrl());

        Post post = new Post(postRequest.getImageUrl(), postRequest.getCaption());

        post = postRepository.save(post);
        postEventSender.sendPostCreated(post);

        log.info("post {} is saved successfully for user {}",
                post.getId(), post.getUsername());

        return post;
    }

    public void deletePost(String postId, String username) {
        log.info("deleting post {}", postId);

        postRepository
                .findById(postId)
                .map(post -> {
                    if(!post.getUsername().equals(username)) {
                        log.warn("user {} is not allowed to delete post id {}", username, postId);
                        throw new NotAllowedException(username, "post id " + postId, "delete");
                    }

                    postRepository.delete(post);
                    postEventSender.sendPostDeleted(post);
                    return post;
                })
                .orElseThrow(() -> {
                    log.warn("post not found id {}", postId);
                    return new ResourceNotFoundException(postId);
                });
    }

    public void likePost(String postId, String username){
        log.info("liking post {} by {}", postId, username);
        postRepository
                .findById(postId)
                .map(post-> {
                    post.getLikes().add(username);
//                    post.setLikes();
                    postRepository.save(post);
                    return post;
                })
                .orElseThrow(() -> {
                    log.warn("post not found id {}", postId);
                    return new ResourceNotFoundException(postId);
                });
    }public void removeLikePost(String postId, String username){
        log.info("removing like post {} by {}", postId, username);
        postRepository
                .findById(postId)
                .map(post-> {
                    if(!post.getLikes().contains(username)) {
                        log.warn("user {} has not liked post {} to begin with", username, postId);
                        throw new NotAllowedException(username, "post id " + postId, "remove like");
                    }
                    post.getLikes().remove(username);
//                    post.setLikes();
                    postRepository.save(post);
                    return post;
                })
                .orElseThrow(() -> {
                    log.warn("post not found id {}", postId);
                    return new ResourceNotFoundException(postId);
                });
    }

    public List<Post> postsByUsername(String username) {
        return postRepository.findByUsernameOrderByCreatedAtDesc(username);
    }

    public List<Post> postsByIdIn(List<String> ids) {
        return postRepository.findByIdInOrderByCreatedAtDesc(ids);
    }

    public void updateCommentNum(String id, int num){
        log.info("updating number of comments in post {}", id);
        Post postNum = new Post();
        postNum.setId(id);
        postNum.setCommentsNum(num);
        postRepository.save(postNum);
    }
}