package ma.ac.inpt.postservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {



    private String _id;
    private String username;

    private String userProfilePic;

    private String caption;

    private  ArrayList<String> likes;

    private  Map<String, Map<String, Integer>> skills;



}