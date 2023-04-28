package ma.ac.inpt.authservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Abstract class for tokens used in the system.
 * Provides common fields and functionality for all token types.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Token {

    /**
     * Unique identifier for the token.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The user associated with the token.
     */
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    /**
     * The token string value.
     */
    @Column(nullable = false, unique = true)
    private String token;

    /**
     * The date and time the token expires.
     */
    @Column(nullable = false)
    private LocalDateTime expiryDate;

    /**
     * Sets the expiry date of the token to 24 hours from the current date and time before persisting it to the database.
     */
    @PrePersist
    public void setExpiryDate() {
        expiryDate = LocalDateTime.now().plusDays(1);
    }

}

