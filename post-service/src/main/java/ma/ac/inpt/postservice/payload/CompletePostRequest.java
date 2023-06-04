package ma.ac.inpt.postservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Optional;

@Data
@AllArgsConstructor
public class PostRequest {



    private String _id;
    private String username;

    private final String userProfilePic;

    private final String caption;

    private  ArrayList<String> likes;

    private  ArrayList<String> skills;


    public PostRequest(String username, String userProfilePic, String caption, ArrayList<String> likes, ArrayList<String> skills) {
        this.username = username;
        this.userProfilePic = userProfilePic;
        this.caption = caption;
        this.likes = likes;
        this.skills = skills;
    }

    public PostRequest(String _id, String userProfilePic, String caption) {
        this._id = _id;
        this.userProfilePic = userProfilePic;
        this.caption = caption;
    }
}