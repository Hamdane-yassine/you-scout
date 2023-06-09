package ma.ac.inpt.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ac.inpt.authservice.util.ValidAuthenticationRequest;

/**
 * Represents an authentication request containing the necessary information to authenticate a user
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ValidAuthenticationRequest(message = "The authentication request is not valid.")
public class AuthenticationRequest {

    /**
     * The type of grant, e.g. "PASSWORD" or "REFRESH_TOKEN".
     */
    private String grantType;
    /**
     * The username of the user.
     */
    private String username;
    /**
     * The password of the user.
     */
    private String password;
    /**
     * A boolean indicating whether to include a refresh token in the response.
     */
    private boolean withRefreshToken;
    /**
     * The refresh token to use for refreshing the access token.
     */
    private String refreshToken;
}
