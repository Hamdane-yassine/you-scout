package ma.ac.inpt.authservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailPayload {

    private String recipientAddress;
    private String subject;
    private String content;

}
