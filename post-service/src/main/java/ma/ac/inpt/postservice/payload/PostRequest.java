package ma.ac.inpt.postservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Optional;

@Data
@AllArgsConstructor
public class PostRequest {




    private final String username;

    private final String userProfilePic;

    private final String video;

    private final String caption;

    private ArrayList<String> likes;

    private ArrayList<String> skills;

}