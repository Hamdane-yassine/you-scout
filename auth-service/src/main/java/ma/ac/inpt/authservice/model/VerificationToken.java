package ma.ac.inpt.authservice.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.ac.inpt.authservice.dto.EmailVerificationType;
import ma.ac.inpt.authservice.dto.UserEventType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Represents a verification token for a user account in the application.
 * Extends the abstract Token class, which provides common token functionality such as the token string and expiry date.
 */
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@Entity
@Table(name = "account_verification_token")
public class VerificationToken extends Token {

    // No additional fields or methods defined in this class, inherits all fields and methods from Token

    @Column(nullable = false)
    private EmailVerificationType emailVerificationType;
}

