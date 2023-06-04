package inpt.ac.ma.chatservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.time.Instant;
import java.util.UUID;


@Table("user_room")
@Data
@Builder
@ToString
public class CRoom {

    @Indexed
    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id; // The username, used as a partition key in the Cassandra table

    @PrimaryKeyColumn(name = "chat_id", ordinal = 1)
    private UUID chatId; // The user ID, used as a clustering key in the Cassandra table



    @PrimaryKeyColumn(name = "sender_id", ordinal = 2, ordering = Ordering.DESCENDING)
    private Instant senderId; // The timestamp of when the user feed entity was created

    @PrimaryKeyColumn(name = "recipient_id", ordinal = 3)
    private String recepientId; // The ID of the associated post
}



