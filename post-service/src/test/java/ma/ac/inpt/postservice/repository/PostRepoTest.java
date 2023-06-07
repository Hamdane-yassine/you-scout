// package ma.ac.inpt.postservice.repository;

// import ma.ac.inpt.postservice.model.Post;
// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

// import java.util.List;

// @DataMongoTest
// public class PostRepoTest {

//     @Autowired
//     private PostRepo postRepo;

//     @Test
//     public void testFindByUsernameOrderByCreatedAtDesc() {
//         // Create some test posts
//         Post post1 = new Post("username", "profilePic",  "that's the stuff");
//         postRepo.save(post1);
//         Post post2 = new Post("username2", "profilePic",  "that's the stuff");
//         postRepo.save(post2);

//         // Call the method being tested
//         List<Post> posts = postRepo.findByUsernameOrderByCreatedAtDesc("username2");
//         System.out.println(posts);
//         // Assertions
//         Assertions.assertEquals(post2.getUsername(), posts.get(0).getUsername());
//     }

//     @Test
//     public void testFindByIdInOrderByCreatedAtDesc() {
//         // Create some test posts
//         Post post1 = new Post("username", "profilePic",  "that's the stuff");
//         postRepo.save(post1);
//         Post post2 = new Post("username2", "profilePic", "that's the stuff");
//         postRepo.save(post2);


//         // Call the method being tested
//         Post post1Retrieved = postRepo.findByUsernameOrderByCreatedAtDesc(post1.getUsername()).get(0);
//         Post post2Retrieved = postRepo.findByUsernameOrderByCreatedAtDesc(post2.getUsername()).get(0);

//         // Assertions

//         Assertions.assertEquals(post1.getUsername(), post1Retrieved.getUsername());
//         Assertions.assertEquals(post2.getUsername(), post2Retrieved.getUsername());
//     }
// }
