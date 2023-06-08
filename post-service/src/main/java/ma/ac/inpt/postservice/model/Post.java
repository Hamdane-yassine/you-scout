package ma.ac.inpt.postservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Document
public class Post {

    private String _id;
    @CreatedDate
    @NotNull
    private Instant createdAt;

    @CreatedBy
    @NotNull
    private String username;

    private String userProfilePic;

    private String videoUrl;

    private String caption;

    private HashSet<String> likes;
    private int commentsNum;

    private Map<String, Map<String, Integer>> skills;

}
