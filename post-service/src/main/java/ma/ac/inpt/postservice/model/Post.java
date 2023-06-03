package ma.ac.inpt.postservice.model;


import lombok.*;
import org.jetbrains.annotations.NotNull;
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

    private String videoUrl;

    @NonNull
    private String caption;

    @NonNull
    private ArrayList<String> likes;

    private int commentsNum;

    @NonNull
    private ArrayList<String> skills;

    private Map<String, Integer> rates;

    /**
     * Constructs a Post object with the specified id, username, profile picture, video, and caption.
     *
     * @param id           The id of the post
     * @param username     The username of the post creator
     * @param profilePic   The profile picture of the post creator
     * @param video        The video URL of the post
     * @param caption      The caption of the post
     */
    public Post(@NotNull String id, @NotNull String username, @NotNull String profilePic, String video, @NotNull String caption) {
        this.id = id;
        this.username = username;
        this.userProfilePic = profilePic;
        this.videoUrl = video;
        this.caption = caption;
    }
}
