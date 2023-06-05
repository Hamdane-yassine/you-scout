package ma.ac.inpt.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ac.inpt.kafkaMessaging.PostEventType;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostEvent {

    private String id;
    private Instant createdAt;
    private String username;
    private String accessToken;
    private String userProfilePic;
    private int likes;
    private int comments;
    private PostEventType eventType;

}