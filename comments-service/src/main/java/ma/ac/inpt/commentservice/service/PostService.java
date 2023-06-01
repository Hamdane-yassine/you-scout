package ma.ac.inpt.commentservice.service;

import ma.ac.inpt.commentservice.exceptions.PostException;
import ma.ac.inpt.commentservice.model.Post;
import ma.ac.inpt.commentservice.repository.CommentRepository;
import ma.ac.inpt.commentservice.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository,
                       CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public Post createPost(Post post){
        String ID = post.getId();
        List<String> comments = new ArrayList<>();
        Post newPost = new Post(ID, comments);
        postRepository.save(newPost);
        return newPost;
    }

    public String deletePost(String postId) {
        Optional<Post> providedPost = postRepository.findById(postId);

        Post post = providedPost.orElseThrow(() -> new PostException("Post not found"));
        try {
            post.getComments().forEach(commentRepository::deleteById);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        postRepository.deleteById(postId);
        return "Post deleted!";
    }
}
