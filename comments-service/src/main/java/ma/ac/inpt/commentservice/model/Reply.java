package ma.ac.inpt.commentservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("replies")
public class Reply {
    @Id
    private String id;
    private User author;
    private String body;
    private String repliedTo;
    private LocalDateTime timestamp;
}
