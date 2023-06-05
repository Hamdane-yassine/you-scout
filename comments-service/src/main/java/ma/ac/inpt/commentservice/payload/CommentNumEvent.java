package ma.ac.inpt.commentservice.payload;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentNumEvent {
    private String id;
    private int commentNum;
}
