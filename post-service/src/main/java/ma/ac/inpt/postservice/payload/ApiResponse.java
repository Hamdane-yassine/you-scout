package ma.ac.inpt.postservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {

    private String message;

    private String postId;

    private String videoUrl;
}