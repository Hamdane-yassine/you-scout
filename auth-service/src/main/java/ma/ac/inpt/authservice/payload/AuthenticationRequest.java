package ma.ac.inpt.authservice.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ac.inpt.authservice.util.ValidAuthenticationRequest;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ValidAuthenticationRequest
public class AuthenticationRequest {

    private String grantType; // Type of grant, e.g. "password" or "refresh_token"

    private String username; // User's username

    private String password; // User's password

    private boolean withRefreshToken; // Boolean value indicating if a new refresh token should be generated

    private String refreshToken; // Refresh token used for obtaining a new access token

}
