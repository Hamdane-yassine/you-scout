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

    private String _id;
    @CreatedDate
    @NonNull
    private Instant createdAt;

    @CreatedBy
    @NonNull
    private String username;

    private String userProfilePic;

    private String videoUrl;

    private String caption;

    private ArrayList<String> likes;
    private int commentsNum;

    private Map<String, Map<String, Integer>> skills;



    /**
     * Constructs a Post object with the specified id, username, profile picture, video, and caption.
     *
     * @param username     The username of the post creator
     * @param profilePic   The profile picture of the post creator
     * @param video        The video URL of the post
     * @param caption      The caption of the post
     */
    public Post( @NotNull String username, @NotNull String profilePic, String video, @NotNull String caption) {
        this.username = username;
        this.userProfilePic = profilePic;
        this.videoUrl = video;
        this.caption = caption;
    }

    public Post(@NotNull String username, String videoUrl) {
        this.username = username;
        this.videoUrl = videoUrl;
        this.createdAt = Instant.now();
    }
}
