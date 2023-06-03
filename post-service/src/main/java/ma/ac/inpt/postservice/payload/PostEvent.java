package ma.ac.inpt.postservice.payload;

import ma.ac.inpt.postservice.postMessaging.PostEventType;
import lombok.Builder;
import lombok.Data;
import java.time.Instant;

@Data
@Builder
public class PostEvent {

    private String id;
    private Instant createdAt;
    private String username;
    private int likes;
    private int comments;
    private PostEventType eventType;
}