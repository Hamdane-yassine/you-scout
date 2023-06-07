package ma.ac.inpt.postservice.payload;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class CommentNumEvent {
    private String id;
    private int commentNum;
}
