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

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FeedServiceTest {

    private FeedService feedService;

    @Mock
    private Cassandra feedRepository;


    @Mock
    private PostService postService;

    @BeforeEach
    public void setUp() {
        feedRepository = Mockito.mock(Cassandra.class);
        postService = Mockito.mock(PostService.class);
        feedService = new FeedService(feedRepository, postService);
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
        when(mockSlice.isLast()).thenReturn(true); // Set the slice as the last page

        // Mock the PostService to return posts
        List<Post> mockPosts = List.of(Post.builder().userProfilePic("profile.jpg").build());
        when(postService.findPostsIn(anyList(),eq("access"))).thenReturn(mockPosts);


        // Invoke the getUserFeed method
        SlicedResult<Post> result = feedService.getUserFeed(username, pagingState, "access");
        // Assert the expected behavior and result
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockPosts, result.getContent());
        Assertions.assertTrue(result.isLast());

        // Verify the method invocations
        verify(feedRepository, times(1)).findByUsername(eq(username), any(CassandraPageRequest.class));
        verify(postService, times(1)).findPostsIn(anyList(), eq("access"));
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
            feedService.getUserFeed(username, pagingState, "access");
        });


        // Verify the method invocation
        verify(feedRepository, times(1)).findByUsername(eq(username), any(CassandraPageRequest.class));
    }
}