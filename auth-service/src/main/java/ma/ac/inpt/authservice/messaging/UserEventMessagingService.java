package ma.ac.inpt.authservice.messaging;

import ma.ac.inpt.authservice.model.User;

/**
 * This interface defines the contract for a messaging service that sends events related to user creation, update, and deletion.
 * Implementations of this interface are responsible for sending messages to a message broker or other messaging system.
 */
public interface UserEventMessagingService {
    /**
     * Sends a message indicating that a new user has been created.
     *
     * @param user The user that was created.
     */
    void sendUserCreated(User user);

    /**
     * Sends a message indicating that an existing user has been updated.
     *
     * @param user The user that was updated.
     */
    void sendUserUpdated(User user);

    /**
     * Sends a message indicating that an existing user has been deleted.
     *
     * @param user The user that was deleted.
     */
    void sendUserDeleted(User user);
}

