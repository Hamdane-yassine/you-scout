package ma.ac.inpt.postservice.model;


import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Post {

    @Id
    @NonNull
    private String id;

    @CreatedDate
    private Instant createdAt;

    @CreatedBy
    @NonNull
    private String username;

    @NonNull
    private String userProfilePic;

    private String video;

    @NonNull
    private String caption;
    @NonNull
    private ArrayList<String> likes;

    private int commentsNum;
    @NonNull
    private ArrayList<String> skills;

    private Map<String, Integer> rates;

    public Post(String id, String username, String profilePic, String video, String s) {
        this.id=id;
        this.username=username;
        this.userProfilePic=profilePic;
        this.video=video;
        this.caption=s;
    }
}