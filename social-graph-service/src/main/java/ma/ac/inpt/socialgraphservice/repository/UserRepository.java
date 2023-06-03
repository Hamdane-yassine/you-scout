package ma.ac.inpt.socialgraphservice.repository;

import ma.ac.inpt.socialgraphservice.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository interface for managing User entities in the Neo4j database.
 */
public interface UserRepository extends Neo4jRepository<User, Long> {

    /**
     * Checks if a user with the given username exists.
     *
     * @param username the username to check
     * @return true if a user with the username exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Unfollows a user by deleting the FOLLOWS relationship between the follower and followee users.
     *
     * @param followerUsername the username of the follower user
     * @param followeeUsername the username of the followee user
     */
    @Query("MATCH (follower:User {username: $followerUsername})-[r:FOLLOWS]->(followee:User {username: $followeeUsername}) DETACH DELETE r")
    void unfollowUser(@Param("followerUsername") String followerUsername, @Param("followeeUsername") String followeeUsername);

    /**
     * Checks if a user is following another user.
     *
     * @param followerUsername the username of the follower user
     * @param followeeUsername the username of the followee user
     * @return true if the follower user is following the followee user, false otherwise
     */
    @Query("RETURN exists((:User {username: $followerUsername})-[:FOLLOWS]->(:User {username: $followeeUsername}))")
    boolean isFollowing(@Param("followerUsername") String followerUsername, @Param("followeeUsername") String followeeUsername);

    /**
     * Retrieves a page of follower usernames for a given username.
     *
     * @param username the username of the user
     * @param pageable the pagination information
     * @return a page of follower usernames
     */
    @Query(value = "MATCH (follower:User)-[:FOLLOWS]->(followee:User {username: $username}) RETURN follower.username",
            countQuery = "MATCH (follower:User)-[:FOLLOWS]->(followee:User {username: $username}) RETURN count(follower)")
    Page<String> findFollowerUsernamesByUsername(@Param("username") String username, Pageable pageable);

    /**
     * Retrieves a page of following usernames for a given username.
     *
     * @param username the username of the user
     * @param pageable the pagination information
     * @return a page of following usernames
     */
    @Query(value = "MATCH (user:User {username: $username})-[:FOLLOWS]->(following:User) RETURN following.username",
            countQuery = "MATCH (user:User {username: $username})-[:FOLLOWS]->(following:User) RETURN count(following)")
    Page<String> findFollowingUsernamesByUsername(@Param("username") String username, Pageable pageable);

    /**
     * Retrieves a page of blocked usernames for a given username.
     *
     * @param username the username of the user
     * @param pageable the pagination information
     * @return a page of blocked usernames
     */
    @Query(value = "MATCH (user:User {username: $username})-[:BLOCKS]->(blocked:User) RETURN blocked.username",
            countQuery = "MATCH (user:User {username: $username})-[:BLOCKS]->(blocked:User) RETURN count(blocked)")
    Page<String> getBlockedUsernamesByUsername(@Param("username") String username, Pageable pageable);

    /**
     * Checks if a user is blocking another user.
     *
     * @param blockerUsername the username of the blocker user
     * @param blockedUsername the username of the blocked user
     * @return true if the blocker user is blocking the blocked user, false otherwise
     */
    @Query("RETURN exists((:User {username: $blockerUsername})-[:BLOCKS]->(:User {username: $blockedUsername}))")
    boolean isBlocking(@Param("blockerUsername") String blockerUsername, @Param("blockedUsername") String blockedUsername);

    /**
     * Follows a user by creating the FOLLOWS relationship between the follower and followee users.
     *
     * @param followerUsername the username of the follower user
     * @param followeeUsername the username of the followee user
     */
    @Query("MATCH (follower:User {username: $followerUsername}), (followee:User {username: $followeeUsername}) MERGE (follower)-[r:FOLLOWS]->(followee)")
    void followUser(@Param("followerUsername") String followerUsername, @Param("followeeUsername") String followeeUsername);

    /**
     * Blocks a user by creating the BLOCKS relationship between the blocker and blocked users.
     *
     * @param blockerUsername the username of the blocker user
     * @param blockedUsername the username of the blocked user
     */
    @Query("MATCH (blocker:User {username: $blockerUsername}), (blocked:User {username: $blockedUsername}) MERGE (blocker)-[r:BLOCKS]->(blocked)")
    void blockUser(@Param("blockerUsername") String blockerUsername, @Param("blockedUsername") String blockedUsername);

    /**
     * Unblocks a user by deleting the BLOCKS relationship between the blocker and blocked users.
     *
     * @param blockerUsername the username of the blocker user
     * @param blockedUsername the username of the blocked user
     */
    @Query("MATCH (blocker:User {username: $blockerUsername})-[r:BLOCKS]->(blocked:User {username: $blockedUsername}) DETACH DELETE r")
    void unblockUser(@Param("blockerUsername") String blockerUsername, @Param("blockedUsername") String blockedUsername);

    /**
     * Counts the number of followers for a given username.
     *
     * @param username the username of the user
     * @return the count of followers
     */
    @Query("MATCH (:User)-[:FOLLOWS]->(followee:User {username: $username}) RETURN count(*)")
    int countFollowers(@Param("username") String username);

    /**
     * Counts the number of users that a given username is following.
     *
     * @param username the username of the user
     * @return the count of following users
     */
    @Query("MATCH (user:User {username: $username})-[:FOLLOWS]->(:User) RETURN count(*)")
    int countFollowing(@Param("username") String username);

    /**
     * Counts the number of blocked users for a given username.
     *
     * @param username the username of the user
     * @return the count of blocked users
     */
    @Query("MATCH (user:User {username: $username})-[:BLOCKS]->(:User) RETURN count(*)")
    int countBlockedUsers(@Param("username") String username);
}
