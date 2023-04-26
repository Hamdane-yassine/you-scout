package ma.ac.inpt.authservice.model;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken extends Token {

}