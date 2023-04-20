package ma.ac.inpt.service;

import ma.ac.inpt.model.Post;
import ma.ac.inpt.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Post post){
        String ID = post.getId();
        List<String> comments = new ArrayList<>();
        Post newPost = new Post(ID, comments);
        postRepository.save(newPost);
        return newPost;
    }
}
