package inpt.ac.ma.chatservice.model;

import inpt.ac.ma.chatservice.payload.MessageStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Indexed;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.Date;
@Table("user_chat")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chat {

    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String chatId; // The username, used as a partition key in the Cassandra table
    @PrimaryKeyColumn(name = "recipient_name", ordinal = 1, ordering = Ordering.DESCENDING)
    private String recipientName; // The timestamp of when the user feed entity was created

    @PrimaryKeyColumn(name = "conv_id", ordinal = 1, ordering = Ordering.DESCENDING)
    private String convId; // The timestamp of when the user feed entity was created
    @PrimaryKeyColumn(name = "sender_name", ordinal = 2, ordering = Ordering.DESCENDING)
    private String senderName; // The timestamp of when the user feed entity was created

    @PrimaryKeyColumn(name = "content", ordinal = 3)
    private String content; // The ID of the associated post
    @PrimaryKeyColumn(name = "timestamp", ordinal = 4)
    private Date timestamp; // The ID of the associated post
    @PrimaryKeyColumn(name = "status", ordinal = 5)
    private MessageStatus status; // The ID of the associated post
}
