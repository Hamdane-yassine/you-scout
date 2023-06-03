package ma.ac.inpt.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Represents a registration request containing the necessary information to create a new user account.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    /**
     * The username of the user. It must be between 1 and 50 characters long.
     */
    @NotNull(message = "Username cannot be null.")
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters long.")
    private String username;

    /**
     * The full name of the user. It must be between 1 and 50 characters long.
     */
    @NotNull(message = "Full name cannot be null.")
    @Size(min = 1, max = 50, message = "Full name must be between 1 and 50 characters long.")
    private String fullName;

    /**
     * The email address of the user. It must be a valid email format.
     */
    @Email(message = "Email must be in a valid format.")
    private String email;

    /**
     * The password of the user. It must be at least 8 characters long and contain at least one uppercase letter,
     * one lowercase letter, one number, and one special character.
     */
    @NotNull(message = "Password cannot be null.")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

}

