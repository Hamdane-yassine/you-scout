package ma.ac.inpt.postservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Map;

@Data
@AllArgsConstructor
public class CompletePostRequest {



    private String _id;

    private  String userProfilePic;

    private  String caption;

    private  ArrayList<String> likes;

    private  Map<String, Map<String, Integer>> skills;


}