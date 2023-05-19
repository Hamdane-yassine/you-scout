package ma.ac.inpt.authservice.dto;

/**
 * Represents the types of user events that can occur.
 * This enum contains the event types: CREATED, UPDATED, and DELETED.
 */
public enum UserEventType {

    /**
     * Indicates that a user was created.
     */
    CREATED,
    /**
     * Indicates that a user was updated.
     */
    UPDATED,
    /**
     * Indicates that a user was deleted.
     */
    DELETED
}
