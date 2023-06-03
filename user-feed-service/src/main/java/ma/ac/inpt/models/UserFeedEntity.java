package ma.ac.inpt.models;


import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table("user_feed")
@Data
@Builder
@ToString
public class UserFeedEntity {

    @Indexed
    @PrimaryKeyColumn(name = "username", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String username; // The username, used as a partition key in the Cassandra table

    @PrimaryKeyColumn(name = "user_id", ordinal = 1)
    private UUID userId; // The user ID, used as a clustering key in the Cassandra table



    @PrimaryKeyColumn(name = "created_at", ordinal = 2, ordering = Ordering.DESCENDING)
    private Instant createdAt; // The timestamp of when the user feed entity was created

    @PrimaryKeyColumn(name = "post_id", ordinal = 3)
    private String postId; // The ID of the associated post
}
