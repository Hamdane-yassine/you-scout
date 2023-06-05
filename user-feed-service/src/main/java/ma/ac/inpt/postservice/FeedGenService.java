package ma.ac.inpt.postservice;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.exceptions.UnableToGetFollowersException;
import ma.ac.inpt.models.UserFeedEntity;
import ma.ac.inpt.models.Post;

import ma.ac.inpt.payload.PagedResult;
import ma.ac.inpt.util.AppConstants;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.feignClient.Graph;
import ma.ac.inpt.repo.Cassandra;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;




@Service
@Slf4j
@RequiredArgsConstructor
public class FeedGenService {

    private final Graph graphClient; // The Graph client used for retrieving followers
    private final Cassandra feedRepository; // The Cassandra repository used for saving feed items


    /**
     * Adds a post to the feed for the specified user.
     *
     * @param post The post to add to the feed.
     * @throws UnableToGetFollowersException If unable to retrieve followers for the user.
     */
    public void addToFeed(Post post, String accessToken) {
        log.info("adding post {} to feed for user {}" ,
                post.getUsername(), post.get_id());


        boolean isLast = false;
        int page = 0;
        int size = AppConstants.PAGE_SIZE;

        while(!isLast) {

            ResponseEntity<PagedResult<String>> response =     graphClient.findFollowers("Bearer "+accessToken,post.getUsername(), page, size);

            if(response.getStatusCode().is2xxSuccessful()) {

                PagedResult<String> result = response.getBody();

                assert result != null;
                log.info("found {} followers for user {}, page {}",
                        result.getTotalElements(), post.getUsername(), page);

                result.getContent()
                        .stream()
                        .map(user -> convertTo(user, post))
                        .forEach(feedRepository::save);

                isLast = result.isLast();
                page++;

            } else {
                String message =
                        String.format("unable to get followers for user %s", post.getUsername());

                log.warn(message);
                throw new UnableToGetFollowersException(message);
            }
        }
    }

    /**
     * Converts a User and a Post object into a UserFeedEntity object.
     *
     * @param user The User object.
     * @param post The Post object.
     * @return The converted UserFeedEntity object.
     */
    private UserFeedEntity convertTo(String user, Post post) {
        return UserFeedEntity
                .builder()
                .username(user)
                .postId(post.get_id())
                .createdAt(post.getCreatedAt())
                .build();
    }
}