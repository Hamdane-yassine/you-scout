package ma.ac.inpt.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ac.inpt.authservice.util.ValidAuthenticationRequest;
import ma.ac.inpt.authservice.util.ValidUserUpdateRequest;

import javax.annotation.Nullable;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidUserUpdateRequest(message = "User update request is invalid")
public class UserUpdateRequest {

    /**
     * The email address of the user.
     * It must be a valid email format.
     */
    @Nullable
    @Email(message = "The email address is not valid.")
    private String newEmail;

    /**
     * The username of the user.
     * It must be between 1 and 50 characters long.
     */
    @Nullable
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters long.")
    private String newUsername;

    /**
     * The password of the user.
     * It must be at least 8 characters long and contain at least one uppercase letter,
     * one lowercase letter, one number, and one special character.
     */
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    /**
     * The password of the user.
     * It must be at least 8 characters long and contain at least one uppercase letter,
     * one lowercase letter, one number, and one special character.
     */
    @Nullable
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String newPassword;

}
