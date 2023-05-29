package ma.ac.inpt.socialgraphservice.repository;

import ma.ac.inpt.socialgraphservice.model.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface UserRepository extends Neo4jRepository<User, Long> {

    boolean existsByUsername(String username);

    @Query("MATCH (follower:User {username: $followerUsername})-[r:FOLLOWS]->(followee:User {username: $followeeUsername}) " + "DETACH DELETE r")
    void unfollowUser(@Param("followerUsername") String followerUsername, @Param("followeeUsername") String followeeUsername);

    @Query("RETURN exists((:User {username: $followerUsername})-[:FOLLOWS]->(:User {username: $followeeUsername}))")
    boolean isFollowing(@Param("followerUsername") String followerUsername, @Param("followeeUsername") String followeeUsername);

    @Query("MATCH (follower:User)-[:FOLLOWS]->(followee:User {username: $username}) RETURN follower")
    Set<User> findFollowers(@Param("username") String username);

    @Query("MATCH (user:User {username: $username})-[:FOLLOWS]->(following:User) RETURN following")
    Set<User> findFollowing(@Param("username") String username);

    @Query("RETURN exists((:User {username: $blockerUsername})-[:BLOCKS]->(:User {username: $blockedUsername}))")
    boolean isBlocking(@Param("blockerUsername") String blockerUsername, @Param("blockedUsername") String blockedUsername);

    @Query("MATCH (user:User {username: $username})-[:BLOCKS]->(blocked:User) RETURN blocked")
    Set<User> getBlockedUsers(@Param("username") String username);

    @Query("MATCH (follower:User {username: $followerUsername}), (followee:User {username: $followeeUsername}) " + "MERGE (follower)-[r:FOLLOWS]->(followee)")
    void followUser(@Param("followerUsername") String followerUsername, @Param("followeeUsername") String followeeUsername);

    @Query("MATCH (blocker:User {username: $blockerUsername}), (blocked:User {username: $blockedUsername}) " + "MERGE (blocker)-[r:BLOCKS]->(blocked)")
    void blockUser(@Param("blockerUsername") String blockerUsername, @Param("blockedUsername") String blockedUsername);

    @Query("MATCH (blocker:User {username: $blockerUsername})-[r:BLOCKS]->(blocked:User {username: $blockedUsername}) " + "DETACH DELETE r")
    void unblockUser(@Param("blockerUsername") String blockerUsername, @Param("blockedUsername") String blockedUsername);

    @Query("MATCH (:User)-[:FOLLOWS]->(followee:User {username: $username}) RETURN count(*)")
    int countFollowers(@Param("username") String username);

    @Query("MATCH (user:User {username: $username})-[:FOLLOWS]->(:User) RETURN count(*)")
    int countFollowing(@Param("username") String username);

    @Query("MATCH (user:User {username: $username})-[:BLOCKS]->(:User) RETURN count(*)")
    int countBlockedUsers(@Param("username") String username);

}
