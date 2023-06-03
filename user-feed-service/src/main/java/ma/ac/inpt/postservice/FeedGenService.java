package ma.ac.inpt.postservice;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.exceptions.UnableToGetFollowersException;
import ma.ac.inpt.UserFeedEntity;
import ma.ac.inpt.models.Post;
import ma.ac.inpt.models.User;
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

    private final Graph graphClient;
    private final Cassandra feedRepository;

    public void addToFeed(Post post) {
        log.info("adding post {} to feed for user {}" ,
                post.getUsername(), post.getId());


        boolean isLast = false;
        int page = 0;
        int size = AppConstants.PAGE_SIZE;

        while(!isLast) {

            ResponseEntity<PagedResult<User>> response =
                    graphClient.findFollowers(post.getUsername(), page, size);

            if(response.getStatusCode().is2xxSuccessful()) {

                PagedResult<User> result = response.getBody();

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

    private UserFeedEntity convertTo(User user, Post post) {
        return UserFeedEntity
                .builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .postId(post.getId())
                .createdAt(post.getCreatedAt())
                .build();
    }
}