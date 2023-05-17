package ma.ac.inpt.payload;


import lombok.*;


@Data
@Builder
public class CommentNumEvent {
    private String id;
    private int commentNum;
}
