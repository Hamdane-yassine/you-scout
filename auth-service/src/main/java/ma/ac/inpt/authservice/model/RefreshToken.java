package ma.ac.inpt.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    /**
     * Unique identifier for the refresh token.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The UUID of the refresh token.
     */
    @Column(unique = true, nullable = false)
    private String tokenUuid;

    /**
     * The expiry date of the refresh token.
     */
    @Column(nullable = false)
    private Instant expiryDate;
}

