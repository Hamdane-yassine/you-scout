package ma.ac.inpt.postservice;

import ma.ac.inpt.models.UserFeedEntity;
import ma.ac.inpt.exceptions.ResourceNotFoundException;
import ma.ac.inpt.models.Post;
import ma.ac.inpt.payload.SlicedResult;
import ma.ac.inpt.repo.Cassandra;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FeedServiceTest {

    private FeedService feedService;

    @Mock
    private Cassandra feedRepository;

    @Mock
    private AuthService authService;

    @Mock
    private PostService postService;

    @BeforeEach
    public void setUp() {
        feedRepository = Mockito.mock(Cassandra.class);
        authService = Mockito.mock(AuthService.class);
        postService = Mockito.mock(PostService.class);
        feedService = new FeedService(feedRepository, authService, postService);
    }

    @Test
    public void testGetUserFeed_WithValidUsernameAndPagingState_ReturnsSlicedResult() {
        // Mocking the required objects and data
        String username = "testuser";
        Optional<String> pagingState = Optional.empty();

        // Mock the Cassandra repository to return a non-empty slice of UserFeedEntity
        Slice<UserFeedEntity> mockSlice = Mockito.mock(Slice.class);
        when(feedRepository.findByUsername(eq(username), any(CassandraPageRequest.class))).thenReturn(mockSlice);
        when(mockSlice.isEmpty()).thenReturn(false);

        // Mock the PostService to return posts
        List<Post> mockPosts = Collections.singletonList(Post.builder().build());
        when(postService.findPostsIn(anyList())).thenReturn(mockPosts);

        // Mock the AuthService to return users' profile pictures
        Map<String, String> mockProfilePics = Collections.singletonMap("testuser", "profile.jpg");
        when(authService.usersProfilePic(anyList())).thenReturn(mockProfilePics);

        // Invoke the getUserFeed method
        SlicedResult<Post> result = feedService.getUserFeed(username, pagingState);

        // Assert the expected behavior and result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockPosts, result.getContent());
        Assertions.assertFalse(result.isLast());
        Assertions.assertEquals(mockProfilePics.get(username), result.getContent().get(0).getUserProfilePic());

        // Verify the method invocations
        verify(feedRepository, times(1)).findByUsername(eq(username), any(CassandraPageRequest.class));
        verify(postService, times(1)).findPostsIn(anyList());
        verify(authService, times(1)).usersProfilePic(anyList());
    }

    @Test
    public void testGetUserFeed_WithInvalidUsername_ThrowsResourceNotFoundException() {
        // Mocking the required objects and data
        String username = "invaliduser";
        Optional<String> pagingState = Optional.empty();

        // Mock the Cassandra repository to return an empty slice of UserFeedEntity
        Slice<UserFeedEntity> mockSlice = Mockito.mock(Slice.class);
        when(feedRepository.findByUsername(eq(username), any(CassandraPageRequest.class))).thenReturn(mockSlice);
        when(mockSlice.isEmpty()).thenReturn(true);

        // Assert that a ResourceNotFoundException is thrown
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            feedService.getUserFeed(username, pagingState);
        });


        // Verify the method invocation
        verify(feedRepository, times(1)).findByUsername(eq(username), any(CassandraPageRequest.class));
    }
}