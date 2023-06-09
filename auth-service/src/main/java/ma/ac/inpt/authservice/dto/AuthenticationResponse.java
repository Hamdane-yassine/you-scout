package ma.ac.inpt.authservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an authentication response containing the necessary information to authenticate a user
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    /**
     * The JWT access token used for authentication.
     */
    @JsonProperty("access_token") // Specifies the name of the JSON property
    private String accessToken;
    /**
     * The JWT refresh token used for refreshing the access token.
     */
    @JsonProperty("refresh_token") // Specifies the name of the JSON property
    private String refreshToken;
}
