package ma.ac.inpt.postservice.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Document
public class Post {

    @Id
    private String id;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @CreatedBy
    private String username;

    @LastModifiedBy
    private String lastModifiedBy;

    private ArrayList<String> likes;

    @NonNull
    private String imageUrl; //for now post will contain only one image

    @NonNull
    private String caption;

    private int commentsNum;
}