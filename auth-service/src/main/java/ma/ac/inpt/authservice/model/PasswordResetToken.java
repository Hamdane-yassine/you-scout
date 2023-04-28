package ma.ac.inpt.authservice.model;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Concrete subclass of Token representing a password reset token.
 * Inherits from the Token class and uses the SuperBuilder annotation for builder pattern.
 * Maps to the "password_reset_token" table in the database.
 */
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken extends Token {

}
