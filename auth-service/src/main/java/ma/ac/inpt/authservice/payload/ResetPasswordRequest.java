package ma.ac.inpt.authservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequest {


    /**
     * The email address provided by the user during registration.
     * It is mandatory and must be a valid email address format.
     */
    @NotNull
    @Email
    private String email;

    /**
     * The new password provided by the user during registration.
     * It is mandatory and must be at least 8 characters long.
     * It must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.
     */
    @NotNull
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String newPassword;
}
