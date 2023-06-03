package ma.ac.inpt.postservice.repository;

import ma.ac.inpt.postservice.model.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
public class PostRepoTest {
    @Autowired
    private PostRepo postRepo;

    @Test
    public void testFindByUsernameOrderByCreatedAtDesc() {
        // Create some test posts
        Post post1 = new Post("id","username","profilePic","video","that's the stuff");
        postRepo.save(post1);
        Post post2 = new Post("id2","username2","profilePic","video","that's the stuff");
        postRepo.save(post2);


        // Call the method being tested
        List<Post> posts = postRepo.findByUsernameOrderByCreatedAtDesc("username2");
        System.out.println(posts);
        // Assertions
        Assertions.assertEquals(1, posts.size());
        Assertions.assertEquals(post2.getId(), posts.get(0).getId());
    }

    @Test
    public void testFindByIdInOrderByCreatedAtDesc() {
        // Create some test posts
        Post post1 = new Post("id","username","profilePic","video","that's the stuff");
        postRepo.save(post1);
        Post post2 = new Post("id2","username2","profilePic","video","that's the stuff");
        postRepo.save(post2);

        List<String> ids = Arrays.asList(post1.getId(), post2.getId());

        // Call the method being tested
        List<Post> posts = postRepo.findByIdInOrderByCreatedAtDesc(ids);

        // Assertions
        Assertions.assertEquals(2, posts.size());
        Assertions.assertEquals(post1.getId(), posts.get(1).getId());
        Assertions.assertEquals(post2.getId(), posts.get(0).getId());
    }
}
