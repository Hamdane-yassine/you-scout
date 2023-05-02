package ma.ac.inpt.socialgraphservice.controller;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.socialgraphservice.model.User;
import ma.ac.inpt.socialgraphservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/{follower}/follow/{followee}")
    public ResponseEntity<Void> followUser(@PathVariable("follower") String followerUsernameOrEmail,
                                           @PathVariable("followee") String followeeUsernameOrEmail) {
        userService.followUser(followerUsernameOrEmail, followeeUsernameOrEmail);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{follower}/unfollow/{followee}")
    public ResponseEntity<Void> unfollowUser(@PathVariable("follower") String followerUsernameOrEmail,
                                             @PathVariable("followee") String followeeUsernameOrEmail) {
        userService.unfollowUser(followerUsernameOrEmail, followeeUsernameOrEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{follower}/isFollowing/{followee}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable("follower") String followerUsernameOrEmail,
                                               @PathVariable("followee") String followeeUsernameOrEmail) {
        return ResponseEntity.ok(userService.isFollowing(followerUsernameOrEmail, followeeUsernameOrEmail));
    }

    @GetMapping("/{usernameOrEmail}/followers")
    public ResponseEntity<Set<User>> findFollowers(@PathVariable String usernameOrEmail) {
        return ResponseEntity.ok(userService.findFollowers(usernameOrEmail));
    }

    @GetMapping("/{usernameOrEmail}/following")
    public ResponseEntity<Set<User>> findFollowing(@PathVariable String usernameOrEmail) {
        return ResponseEntity.ok(userService.findFollowing(usernameOrEmail));
    }

    @PostMapping("/{blocker}/block/{blocked}")
    public ResponseEntity<Void> blockUser(@PathVariable("blocker") String blockerUsernameOrEmail,
                                          @PathVariable("blocked") String blockedUsernameOrEmail) {
        userService.blockUser(blockerUsernameOrEmail, blockedUsernameOrEmail);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{blocker}/unblock/{blocked}")
    public ResponseEntity<Void> unblockUser(@PathVariable("blocker") String blockerUsernameOrEmail,
                                            @PathVariable("blocked") String blockedUsernameOrEmail) {
        userService.unblockUser(blockerUsernameOrEmail, blockedUsernameOrEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{blocker}/isBlocking/{blocked}")
    public ResponseEntity<Boolean> isBlocking(@PathVariable("blocker") String blockerUsernameOrEmail,
                                              @PathVariable("blocked") String blockedUsernameOrEmail) {
        return ResponseEntity.ok(userService.isBlocking(blockerUsernameOrEmail, blockedUsernameOrEmail));
    }

    @GetMapping("/{usernameOrEmail}/blocked")
    public ResponseEntity<Set<User>> getBlockedUsers(@PathVariable String usernameOrEmail) {
        return ResponseEntity.ok(userService.getBlockedUsers(usernameOrEmail));
    }

    @GetMapping("/{username}/followers/count")
    public ResponseEntity<Integer> countFollowers(@PathVariable String username) {
        int count = userService.countFollowers(username);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{username}/following/count")
    public ResponseEntity<Integer> countFollowing(@PathVariable String username) {
        int count = userService.countFollowing(username);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{username}/blocked/count")
    public ResponseEntity<Integer> countBlockedUsers(@PathVariable String username) {
        int count = userService.countBlockedUsers(username);
        return ResponseEntity.ok(count);
    }

}