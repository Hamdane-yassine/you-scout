package ma.ac.inpt.commentservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("comments")
public class Comment {
    @Id
    private String id;
    private String author;
    private String body;
    private List<String> replies;
    @Indexed
    private String postId;
    private LocalDateTime timestamp;
    private List<String> likes;
}
