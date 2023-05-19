package ma.ac.inpt.socialgraphservice.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

@Data
@Node
public class User {

    @Id
    private Long id;

    private String username;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private Set<User> following;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.INCOMING)
    private Set<User> followers;

    @Relationship(type = "BLOCKS", direction = Relationship.Direction.OUTGOING)
    private Set<User> blockedUsers;

}