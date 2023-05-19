package ma.ac.inpt.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an email payload containing the necessary information to send an email.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailPayload {

    /**
     * The email address of the recipient.
     */
    private String recipientAddress;
    /**
     * The subject line of the email.
     */
    private String subject;
    /**
     * The content of the email.
     */
    private String content;
}
