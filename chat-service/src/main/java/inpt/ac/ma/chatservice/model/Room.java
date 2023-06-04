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

    @PrimaryKeyColumn(name = "chat_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String chatId; // The username, used as a partition key in the Cassandra table

    @Indexed
    private String senderName; // The timestamp of when the user feed entity was created
    @Indexed
    private String recipientName; // The ID of the associated post
}



