package ma.ac.inpt.socialgraphservice.controller;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.socialgraphservice.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for managing user interactions.
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Retrieves the followers of a user with pagination.
     *
     * @param usernameOrEmail the username or email of the user
     * @param page            the page number
     * @param size            the number of followers to retrieve per page
     * @return a ResponseEntity with a Page of follower usernames or emails
     */
    @GetMapping("/{usernameOrEmail}/followers")
    public ResponseEntity<Page<String>> findFollowers(@PathVariable String usernameOrEmail, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "1000") Integer size) {
        return ResponseEntity.ok(userService.findFollowers(usernameOrEmail, page, size));
    }

    /**
     * Retrieves the users that a user is following with pagination.
     *
     * @param usernameOrEmail the username or email of the user
     * @param page            the page number
     * @param size            the number of users to retrieve per page
     * @return a ResponseEntity with a Page of followed usernames or emails
     */
    @GetMapping("/{usernameOrEmail}/following")
    public ResponseEntity<Page<String>> findFollowing(@PathVariable String usernameOrEmail, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "1000") Integer size) {
        return ResponseEntity.ok(userService.findFollowing(usernameOrEmail, page, size));
    }

    /**
     * Retrieves the blocked users of a user with pagination.
     *
     * @param usernameOrEmail the username or email of the user
     * @param page            the page number
     * @param size            the number of blocked users to retrieve per page
     * @return a ResponseEntity with a Page of blocked usernames or emails
     */
    @GetMapping("/{usernameOrEmail}/blocked")
    public ResponseEntity<Page<String>> getBlockedUsers(@PathVariable String usernameOrEmail, @RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "1000") Integer size) {
        return ResponseEntity.ok(userService.getBlockedUsers(usernameOrEmail, page, size));
    }

    /**
     * Follows a user.
     *
     * @param followerUsernameOrEmail the username or email of the follower
     * @param followeeUsernameOrEmail the username or email of the followee
     * @return a ResponseEntity with no content
     */
    @PostMapping("/{follower}/follow/{followee}")
    public ResponseEntity<Void> followUser(@PathVariable("follower") String followerUsernameOrEmail,
                                           @PathVariable("followee") String followeeUsernameOrEmail) {
        userService.followUser(followerUsernameOrEmail, followeeUsernameOrEmail);
        return ResponseEntity.noContent().build();
    }

    /**
     * Unfollows a user.
     *
     * @param followerUsernameOrEmail the username or email of the follower
     * @param followeeUsernameOrEmail the username or email of the followee
     * @return a ResponseEntity with no content
     */
    @PostMapping("/{follower}/unfollow/{followee}")
    public ResponseEntity<Void> unfollowUser(@PathVariable("follower") String followerUsernameOrEmail,
                                             @PathVariable("followee") String followeeUsernameOrEmail) {
        userService.unfollowUser(followerUsernameOrEmail, followeeUsernameOrEmail);
        return ResponseEntity.noContent().build();
    }

    /**
     * Checks if a user is following another user.
     *
     * @param followerUsernameOrEmail the username or email of the follower
     * @param followeeUsernameOrEmail the username or email of the followee
     * @return a ResponseEntity with a boolean indicating if the user is following
     */
    @GetMapping("/{follower}/isFollowing/{followee}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable("follower") String followerUsernameOrEmail,
                                               @PathVariable("followee") String followeeUsernameOrEmail) {
        return ResponseEntity.ok(userService.isFollowing(followerUsernameOrEmail, followeeUsernameOrEmail));
    }

    /**
     * Blocks a user.
     *
     * @param blockerUsernameOrEmail the username or email of the blocker
     * @param blockedUsernameOrEmail the username or email of the blocked user
     * @return a ResponseEntity with no content
     */
    @PostMapping("/{blocker}/block/{blocked}")
    public ResponseEntity<Void> blockUser(@PathVariable("blocker") String blockerUsernameOrEmail,
                                          @PathVariable("blocked") String blockedUsernameOrEmail) {
        userService.blockUser(blockerUsernameOrEmail, blockedUsernameOrEmail);
        return ResponseEntity.noContent().build();
    }

    /**
     * Unblocks a user.
     *
     * @param blockerUsernameOrEmail the username or email of the blocker
     * @param blockedUsernameOrEmail the username or email of the blocked user
     * @return a ResponseEntity with no content
     */
    @PostMapping("/{blocker}/unblock/{blocked}")
    public ResponseEntity<Void> unblockUser(@PathVariable("blocker") String blockerUsernameOrEmail,
                                            @PathVariable("blocked") String blockedUsernameOrEmail) {
        userService.unblockUser(blockerUsernameOrEmail, blockedUsernameOrEmail);
        return ResponseEntity.noContent().build();
    }

    /**
     * Checks if a user is blocking another user.
     *
     * @param blockerUsernameOrEmail the username or email of the blocker
     * @param blockedUsernameOrEmail the username or email of the blocked user
     * @return a ResponseEntity with a boolean indicating if the user is blocking
     */
    @GetMapping("/{blocker}/isBlocking/{blocked}")
    public ResponseEntity<Boolean> isBlocking(@PathVariable("blocker") String blockerUsernameOrEmail,
                                              @PathVariable("blocked") String blockedUsernameOrEmail) {
        return ResponseEntity.ok(userService.isBlocking(blockerUsernameOrEmail, blockedUsernameOrEmail));
    }

    /**
     * Retrieves the count of followers for a user.
     *
     * @param username the username of the user
     * @return a ResponseEntity with the count of followers
     */
    @GetMapping("/{username}/followers/count")
    public ResponseEntity<Integer> countFollowers(@PathVariable String username) {
        int count = userService.countFollowers(username);
        return ResponseEntity.ok(count);
    }

    /**
     * Retrieves the count of users that a user is following.
     *
     * @param username the username of the user
     * @return a ResponseEntity with the count of following users
     */
    @GetMapping("/{username}/following/count")
    public ResponseEntity<Integer> countFollowing(@PathVariable String username) {
        int count = userService.countFollowing(username);
        return ResponseEntity.ok(count);
    }

    /**
     * Retrieves the count of blocked users for a user.
     *
     * @param username the username of the user
     * @return a ResponseEntity with the count of blocked users
     */
    @GetMapping("/{username}/blocked/count")
    public ResponseEntity<Integer> countBlockedUsers(@PathVariable String username) {
        int count = userService.countBlockedUsers(username);
        return ResponseEntity.ok(count);
    }

}