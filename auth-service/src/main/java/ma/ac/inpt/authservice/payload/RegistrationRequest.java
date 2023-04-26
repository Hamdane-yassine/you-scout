package ma.ac.inpt.authservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    /**
     * The username provided by the user during registration.
     * It is mandatory and can have a length between 1 and 50 characters.
     */
    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String username;

    /**
     * The full name provided by the user during registration.
     * It is mandatory and can have a length between 1 and 50 characters.
     * It must contain only letters and spaces.
     */
    @NotNull
    @NotEmpty
    @Size(max = 50)
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Full name can only contain letters and spaces")
    private String fullName;

    /**
     * The email address provided by the user during registration.
     * It is mandatory and must be a valid email address format.
     */
    @NotNull
    @Email
    private String email;

    /**
     * The password provided by the user during registration.
     * It is mandatory and must be at least 8 characters long.
     * It must contain at least one uppercase letter, one lowercase letter, one digit, and one special character.
     */
    @NotNull
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*_?&]+$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;

}
