package ma.ac.inpt.controller;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.models.Post;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.payload.SlicedResult;
import ma.ac.inpt.postservice.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/feed/{username}")
    public ResponseEntity<SlicedResult<Post>> getFeed(
            @PathVariable String username,
            @RequestParam(value = "ps", required = false) Optional<String> pagingState,
            @RequestHeader("Authorization") String accessToken) {

        log.info("fetching feed for user {} isFirstPage {}", username, pagingState.isEmpty());
        SlicedResult<Post> results = feedService.getUserFeed(username, pagingState, accessToken);
        return ResponseEntity.ok(results);
    }
}
