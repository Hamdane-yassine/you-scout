package ma.ac.inpt.socialgraphservice.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Objects;
import java.util.Set;

/**
 * Data class representing a User node in the graph database.
 */
@Data
@Node
public class User {

    /**
     * The unique identifier of the user.
     */
    @Id
    private Long id;

    /**
     * The username of the user.
     */
    private String username;

    /**
     * The users that this user is following.
     */
    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private Set<User> following;

    /**
     * The users that are following this user.
     */
    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.INCOMING)
    private Set<User> followers;

    /**
     * The users that this user has blocked.
     */
    @Relationship(type = "BLOCKS", direction = Relationship.Direction.OUTGOING)
    private Set<User> blockedUsers;

    /**
     * Overrides the equals() method to compare users based on their username.
     *
     * @param o the object to compare
     * @return true if the users are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    /**
     * Generates the hash code for the user based on its properties.
     *
     * @return the hash code of the user
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, username, following, followers, blockedUsers);
    }
}
