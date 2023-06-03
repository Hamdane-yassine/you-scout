package ma.ac.inpt.postservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Optional;

@Data
@AllArgsConstructor
public class PostRequest {

    private String id;

    private String username;

    private String userProfilePic;

    private String video;

    private String caption;

    private Optional<ArrayList<String>> likes;

    private Optional<ArrayList<String>> skills;

}