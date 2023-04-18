package ma.ac.inpt.authservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    // The username provided by the user during registration
    @NotNull
    private String username;

    // The first name provided by the user during registration
    @NotNull
    private String firstname;

    // The last name provided by the user during registration
    @NotNull
    private String lastname;

    // The email address provided by the user during registration
    @NotNull
    @Email
    private String email;

    // The password provided by the user during registration. Must be at least 8 characters long
    @NotNull
    @Size(min = 8)
    private String password;

    // A flag indicating whether the response should include a refresh token
    private boolean withRefreshToken;

}
