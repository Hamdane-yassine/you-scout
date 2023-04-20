package ma.ac.inpt.service;

import ma.ac.inpt.exceptions.CommentNotFoundException;
import ma.ac.inpt.exceptions.PostNotFoundException;
import ma.ac.inpt.exceptions.UserNotFoundException;
import ma.ac.inpt.model.Comment;
import ma.ac.inpt.model.Post;
import ma.ac.inpt.model.Reply;
import ma.ac.inpt.model.User;
import ma.ac.inpt.repository.CommentRepository;
import ma.ac.inpt.repository.PostRepository;
import ma.ac.inpt.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository,
                          PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Comment createComment(Comment comment){
        ObjectId objectId = new ObjectId();
        String ID = objectId.toHexString();

        Optional<User> providedUser = userRepository.findById(comment.getId());
        User user = providedUser.orElseThrow(() -> new UserNotFoundException("User not found"));

        List<Reply> replies = new ArrayList<>();

        Optional<Post> providedPost = postRepository.findById(comment.getPostId());
        Post post = providedPost.orElseThrow(() -> new PostNotFoundException("Post not found"));

        List<String> postComments = post.getComments();
        postComments.add(ID);
        post.setComments(postComments);

        Comment newComment = new Comment(ID, user, comment.getBody(), replies, comment.getPostId());
        commentRepository.save(newComment);
        postRepository.save(post);
        return newComment;
    }

    public List<Comment> getAllCommentsForPost(String postId) {
        Optional<Post> providedPost = postRepository.findById(postId);
        Post post = providedPost.orElseThrow(() -> new PostNotFoundException("Post not found"));
        List<String> postComments = post.getComments();
        List<Comment> comments = new ArrayList<>();

        postComments.forEach(comment -> {
                    Optional<Comment> existingComment = commentRepository.findById(comment);
                    Comment confirmedComment = existingComment.orElseThrow(() -> new CommentNotFoundException("Comment not found"));
                    comments.add(confirmedComment);
                });
        return comments;
    }

}
