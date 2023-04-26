package ma.ac.inpt.authservice.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;


@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "account_verification_token")
public class VerificationToken extends Token {

}
