package ma.ac.inpt.authservice.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserEventPayload {

    private Long id;

    private String email;

    private String username;

    private UserEventType userEventType;

}
