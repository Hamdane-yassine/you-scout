package ma.ac.inpt.models;


import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;


@Data
@Builder
@ToString
public class Post {

    private String _id;
    private Instant createdAt;
    private String username;
    private String userProfilePic;
    private String videoUrl;
    private String caption;
    private int commentsNum;
    private ArrayList<String> likes;
    private Map<String, Map<String, Integer>> skills;
}