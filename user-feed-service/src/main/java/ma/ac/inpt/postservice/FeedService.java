package ma.ac.inpt.postservice;


import com.datastax.oss.driver.api.core.cql.PagingState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.models.UserFeedEntity;
import ma.ac.inpt.exceptions.ResourceNotFoundException;
import ma.ac.inpt.models.Post;
import ma.ac.inpt.payload.SlicedResult;
import ma.ac.inpt.repo.Cassandra;
import ma.ac.inpt.util.AppConstants;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.util.*;

import static java.util.stream.Collectors.toList;


@Service
@Slf4j
@RequiredArgsConstructor
public class FeedService {

    private final Cassandra feedRepository; // The Cassandra repository used for retrieving feed data

    private final PostService postService; // The PostService used for retrieving posts

    /**
     * Retrieves the user's feed.
     *
     * @param username    The username of the user.
     * @param pagingState An optional paging state for retrieving subsequent pages of the feed.
     * @return A SlicedResult containing the feed posts and paging information.
     * @throws ResourceNotFoundException If the feed is not found for the user.
     */
    public SlicedResult<Post> getUserFeed(String username, Optional<String> pagingState) {

        log.info("getting feed for user {} isFirstPage {}", username, pagingState.isEmpty());

        // Construct the CassandraPageRequest based on the paging state
        CassandraPageRequest request = pagingState.map(pState ->
                        CassandraPageRequest.of(PageRequest.of(0, AppConstants.PAGE_SIZE),
                                (ByteBuffer) PagingState.fromString(pState)))
                .orElse(CassandraPageRequest.first(AppConstants.PAGE_SIZE));

        // Retrieve a page of UserFeedEntity from the feedRepository
        Slice<UserFeedEntity> page =
                feedRepository.findByUsername(username, request);

        if(page.isEmpty()) {
            throw new ResourceNotFoundException(
                    String.format("Feed not found for user %s", username));
        }

        String pageState = null;

        // Get the paging state for the next page if available
        if (!page.isLast()) {
            pageState = Objects.requireNonNull(((CassandraPageRequest) page.getPageable())
                    .getPagingState()).toString();
        }

        // Retrieve the corresponding posts for the UserFeedEntity objects
        List<Post> posts = getPosts(page);

        return SlicedResult.<Post>builder()
                .content(posts)
                .isLast(page.isLast())
                .pagingState(pageState)
                .build();
    }


    /**
     * Retrieves the posts for the given UserFeedEntity objects.
     *
     * @param page The UserFeedEntity objects.
     * @return A list of posts.
     */
    private List<Post> getPosts(Slice<UserFeedEntity> page) {
//        String token = authService.getAccessToken();

        // Extract the post IDs from the UserFeedEntity objects
        List<String> postIds = page.stream()
                .map(UserFeedEntity::getPostId)
                .collect(toList());

        // Retrieve the posts for the post IDs


        return postService.findPostsIn(postIds);
    }
}