package inpt.ac.ma.chatservice.model;

import lombok.*;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;


@Table("user_room")
@Data
@Builder
@ToString
public class Room {

    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id; // The username, used as a partition key in the Cassandra table
    @Indexed
    @PrimaryKeyColumn(name = "chat_id", ordinal = 1)
    private String chatId; // The user ID, used as a clustering key in the Cassandra table


    @Indexed
    private String senderName; // The timestamp of when the user feed entity was created
    @Indexed
    private String recipientName; // The ID of the associated post
}



