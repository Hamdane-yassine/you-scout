package ma.ac.inpt.controller;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.models.Post;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.payload.SlicedResult;
import ma.ac.inpt.postservice.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    /**
     * Retrieves the feed for a user.
     *
     * @param username    the username of the user
     * @param pagingState optional paging state parameter for pagination
     * @return the ResponseEntity containing the sliced result of posts
     */
    @RequestMapping("/feed/{username}")
    public ResponseEntity<SlicedResult<Post>> getFeed(
            @PathVariable String username,
            @RequestParam(value = "ps", required = false) Optional<String> pagingState) {

        log.info("fetching feed for user {} isFirstPage {}", username, pagingState.isEmpty());

        return ResponseEntity.ok(feedService.getUserFeed(username, pagingState));
    }
}
