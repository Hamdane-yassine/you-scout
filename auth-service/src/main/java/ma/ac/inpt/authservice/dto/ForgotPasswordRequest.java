package ma.ac.inpt.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

/**
 * Represents a forgot password request containing the email address of the user.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordRequest {

    /**
     * The email address of the user.
     */
    @Email(message = "The email address is not valid.")
    private String email;
}
