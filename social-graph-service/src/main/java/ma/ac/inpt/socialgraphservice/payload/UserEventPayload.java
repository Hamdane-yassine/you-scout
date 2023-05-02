package ma.ac.inpt.socialgraphservice.payload;

import lombok.Data;

/**
 * Represents a payload containing the necessary information about a user event.
 * The payload contains information such as the user's ID, email, username, and the type of event that occurred.
 */
@Data
public class UserEventPayload {

    /**
     * The ID of the user.
     */
    private Long id;
    /**
     * The username of the user.
     */
    private String username;
    /**
     * The type of event that occurred, e.g. "CREATED" "UPDATED" or "DELETED".
     */
    private UserEventType userEventType;
}
