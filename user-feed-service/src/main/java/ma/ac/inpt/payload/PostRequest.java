package ma.ac.inpt.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {

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