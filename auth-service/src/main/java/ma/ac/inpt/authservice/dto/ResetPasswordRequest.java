package ma.ac.inpt.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Represents a reset password request containing the email address of the user and their new password.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {

    /**
     * The email address of the user.
     */
    @Email(message = "The email address is not valid.")
    private String email;
    /**
     * The new password of the user.
     * It must be at least 8 characters long and contain at least one uppercase letter,
     * one lowercase letter, one number, and one special character.
     */
    @NotNull(message = "The new password cannot be null.")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;
}
