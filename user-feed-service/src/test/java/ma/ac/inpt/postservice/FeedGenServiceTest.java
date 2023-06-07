package ma.ac.inpt.postservice;

import ma.ac.inpt.models.UserFeedEntity;
import ma.ac.inpt.exceptions.UnableToGetFollowersException;
import ma.ac.inpt.feignClient.Graph;
import ma.ac.inpt.models.Post;

import ma.ac.inpt.payload.PagedResult;
import ma.ac.inpt.repo.Cassandra;
import ma.ac.inpt.util.AppConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.ArrayList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedGenServiceTest {
    @Mock
    private Graph graphClient;

    @Mock
    private Cassandra feedRepository;

    private FeedGenService feedGenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        feedGenService = new FeedGenService(graphClient, feedRepository);
    }

    @Test
    public void testAddToFeed() {
        // Create a Post object for the test
        Post post = Post.builder().build();
        post.set_id("postId");
        post.setUsername("user123");
        post.setCreatedAt(Instant.now());
        // Set the necessary properties of the post

        // Create variables for accessToken, username, page, and size
        String accessToken = "access_token";
        String username = post.getUsername();
        int page = 0;
        int page2 = 1;
        int size = AppConstants.PAGE_SIZE;

        ArrayList<String> followers = new ArrayList<>();
        followers.add(username);
        followers.add(username);

        // Create a PagedResult<String> object for the first mock response
        PagedResult<String> pagedResult1 = PagedResult.<String>builder()
                .content(followers)
                .totalElements(2)
                .last(false)
                .build();
        // Set the necessary properties of the first pagedResult

        // Create a PagedResult<String> object for the second mock response
        PagedResult<String> pagedResult2 = PagedResult.<String>builder()
                .content(List.of(username))
                .totalElements(1)
                .last(true)
                .build();
        // Set the necessary properties of the second pagedResult

        // Create a successful ResponseEntity with the first pagedResult
        ResponseEntity<PagedResult<String>> response1 = new ResponseEntity<>(pagedResult1, HttpStatus.OK);
        // Create a successful ResponseEntity with the second pagedResult
        ResponseEntity<PagedResult<String>> response2 = new ResponseEntity<>(pagedResult2, HttpStatus.OK);
        // Mock the graphClient's findFollowers method
        when(graphClient.findFollowers(accessToken, username, page, size))
                .thenReturn(response1);
        when(graphClient.findFollowers(accessToken, username, page2, size))
                .thenReturn(response2);

        // Call the addToFeed method in the FeedGenService
        feedGenService.addToFeed(post, accessToken);

        // Verify that the graphClient's findFollowers method was called twice with the correct arguments
        verify(graphClient, times(1)).findFollowers(accessToken, username, page, size);
        verify(graphClient, times(1)).findFollowers(accessToken, username, page2, size);

        // Verify that the feedRepository's save method was called for each follower in both responses
        verify(feedRepository, times(followers.size() + 1)).save(any(UserFeedEntity.class));
    }

//    @Test
//    void addToFeed_SuccessfullyAddsPostToFeed() {
//        // Arrange
//        Post post = Post.builder().build();
//        post.set_id("postId");
//        post.setUsername("user123");
//        post.setCreatedAt(Instant.now());
//
//        List<String> followers = new ArrayList<>();
//        followers.add("Follower 1");
//        followers.add("Follower 2");
//
//        PagedResult<String> pagedResult = PagedResult.<String>builder()
//                .content(followers)
//                .totalElements(followers.size())
//                .last(true)
//                .build();
//        ResponseEntity<PagedResult<String>> response = new ResponseEntity<>(pagedResult, HttpStatus.OK);
//
//        when(graphClient.findFollowers("Bearer access",post.getUsername(), 0, 10)).thenReturn(response);
//
//        // Act
//        feedGenService.addToFeed(post, "access");
//
//        // Assert
//        verify(graphClient, times(1)).findFollowers("Bearer access",post.getUsername(), 0, 10);
//        verify(feedRepository, times(followers.size())).save(any(UserFeedEntity.class));
//        verifyNoMoreInteractions(graphClient, feedRepository);
//    }

    @Test
    public void testAddToFeed_UnableToGetFollowersException() {
        // Create a Post object for the test
        Post post = Post.builder().build();
        post.set_id("postId");
        post.setUsername("user123");
        post.setCreatedAt(Instant.now());
        // Set the necessary properties of the post

        // Create variables for accessToken, username, page, and size
        String accessToken = "access_token";
        String username = post.getUsername();
        int page = 0;
        int size = AppConstants.PAGE_SIZE;

        // Create an unsuccessful ResponseEntity with a custom status code and error message
        ResponseEntity<PagedResult<String>> response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        // You can customize the response object based on the specific scenario you want to test

        // Mock the graphClient's findFollowers method
        when(graphClient.findFollowers(accessToken, username, page, size)).thenReturn(response);

        // Verify that the addToFeed method throws the expected exception
        assertThrows(UnableToGetFollowersException.class, () -> {
            feedGenService.addToFeed(post, accessToken);
        });

        // Verify that the graphClient's findFollowers method was called with the correct arguments
        verify(graphClient, times(1)).findFollowers(accessToken, username, page, size);

        // Verify that the feedRepository's save method was not called
        verify(feedRepository, never()).save(any(UserFeedEntity.class));
    }

//    @Test
//    void addToFeed_ThrowsException_WhenUnableToGetFollowers() {
//        // Arrange
//        Post post = Post.builder().build();
//        post.set_id("postId");
//        post.setUsername("user123");
//        post.setCreatedAt(Instant.now());
//
//        ResponseEntity<PagedResult<String>> response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//
//        when(graphClient.findFollowers("Bearer access", post.getUsername(), 0, 10)).thenReturn(response);
//
//        // Act & Assert
//        UnableToGetFollowersException exception = assertThrows(
//                UnableToGetFollowersException.class,
//                () -> feedGenService.addToFeed(post, "access")
//        );
//
//        assertEquals("unable to get followers for user user123", exception.getMessage());
//
//        verify(graphClient, times(1)).findFollowers("Bearer access",post.getUsername(), 0, 10);
//        verifyNoMoreInteractions(graphClient, feedRepository);
//    }
}
