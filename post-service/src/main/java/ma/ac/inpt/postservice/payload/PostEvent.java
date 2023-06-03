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
    private Instant updatedAt;
    private String username;
    private String lastModifiedBy;
    private String imageUrl;
    private String caption;
    private PostEventType eventType;
}