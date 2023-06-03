package ma.ac.inpt.postservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Optional;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PostRequest {



    private final String id;

    private final String username;

    private final String userProfilePic;

    private final String video;

    private final String caption;

    private Optional<ArrayList<String>> likes;

    private Optional<ArrayList<String>> skills;

}