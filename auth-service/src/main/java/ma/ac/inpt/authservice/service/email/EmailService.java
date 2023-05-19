package ma.ac.inpt.authservice.service.email;

import ma.ac.inpt.authservice.dto.EmailPayload;

/**
 * Interface representing a service for sending emails.
 */
public interface EmailService {

    /**
     * Sends an email using the given payload.
     *
     * @param payload the email payload containing the necessary information to send the email.
     */
    void sendEmail(EmailPayload payload);

}