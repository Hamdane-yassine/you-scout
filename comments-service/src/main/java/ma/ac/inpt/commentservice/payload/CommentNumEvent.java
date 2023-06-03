package ma.ac.inpt.commentservice.payload;


import lombok.*;


@Data
@Builder
public class CommentNumEvent {
    private String id;
    private int commentNum;
}
