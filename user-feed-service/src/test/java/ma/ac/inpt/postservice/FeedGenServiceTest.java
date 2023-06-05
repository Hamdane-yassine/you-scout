package ma.ac.inpt.postservice;

import ma.ac.inpt.models.UserFeedEntity;
import ma.ac.inpt.exceptions.UnableToGetFollowersException;
import ma.ac.inpt.feignClient.Graph;
import ma.ac.inpt.models.Post;

import ma.ac.inpt.payload.PagedResult;
import ma.ac.inpt.repo.Cassandra;
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
    void addToFeed_SuccessfullyAddsPostToFeed() {
        // Arrange
        Post post = Post.builder().build();
        post.setId("postId");
        post.setUsername("user123");
        post.setCreatedAt(Instant.now());

        List<String> followers = new ArrayList<>();
        followers.add("Follower 1");
        followers.add("Follower 2");

        PagedResult<String> pagedResult = PagedResult.<String>builder()
                .content(followers)
                .totalElements(followers.size())
                .last(true)
                .build();
        ResponseEntity<PagedResult<String>> response = new ResponseEntity<>(pagedResult, HttpStatus.OK);

        when(graphClient.findFollowers("Bearer adsflkjagk'aj'afg",post.getUsername(), 0, 10)).thenReturn(response);

        // Act
        feedGenService.addToFeed(post);

        // Assert
        verify(graphClient, times(1)).findFollowers("Bearer adsflkjagk'aj'afg",post.getUsername(), 0, 10);
        verify(feedRepository, times(followers.size())).save(any(UserFeedEntity.class));
        verifyNoMoreInteractions(graphClient, feedRepository);
    }

    @Test
    void addToFeed_ThrowsException_WhenUnableToGetFollowers() {
        // Arrange
        Post post = Post.builder().build();
        post.setId("postId");
        post.setUsername("user123");
        post.setCreatedAt(Instant.now());

        ResponseEntity<PagedResult<String>> response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        when(graphClient.findFollowers("Bearer adsflkjagk'aj'afg", post.getUsername(), 0, 10)).thenReturn(response);

        // Act & Assert
        UnableToGetFollowersException exception = assertThrows(
                UnableToGetFollowersException.class,
                () -> feedGenService.addToFeed(post)
        );

        assertEquals("unable to get followers for user user123", exception.getMessage());

        verify(graphClient, times(1)).findFollowers("Bearer adsflkjagk'aj'afg",post.getUsername(), 0, 10);
        verifyNoMoreInteractions(graphClient, feedRepository);
    }
}
