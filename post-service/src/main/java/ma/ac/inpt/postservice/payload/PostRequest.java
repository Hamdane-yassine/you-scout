package ma.ac.inpt.postservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostRequest {

    private String id;
    private String userName;
    private String imageUrl;
    private String caption;
}