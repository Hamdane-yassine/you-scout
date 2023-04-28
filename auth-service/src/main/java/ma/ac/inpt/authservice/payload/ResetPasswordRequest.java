package ma.ac.inpt.authservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", message = "The new password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character.")
    private String newPassword;
}
