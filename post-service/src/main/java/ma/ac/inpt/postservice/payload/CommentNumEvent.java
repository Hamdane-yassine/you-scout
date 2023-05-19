package ma.ac.inpt.postservice.payload;


import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CommentNumEvent {
    private String id;
    private int commentNum;
}
