package ma.ac.inpt.authservice.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a payload containing the necessary information about a user event.
 * The payload contains information such as the user's ID, email, username, and the type of event that occurred.
 */
@Data
@NoArgsConstructor
public class UserEventPayload {

    /**
     * The ID of the user.
     */
    private Long id;
    /**
     * The email address of the user.
     */
    private String email;
    /**
     * The username of the user.
     */
    private String username;
    /**
     * The type of event that occurred, e.g. "USER_CREATED" or "USER_DELETED".
     */
    private UserEventType userEventType;
}
