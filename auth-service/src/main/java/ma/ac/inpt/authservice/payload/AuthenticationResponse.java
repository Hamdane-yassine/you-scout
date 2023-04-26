package ma.ac.inpt.authservice.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_token") // Specifies the name of the JSON property
    private String accessToken; // JWT token used for authentication

    @JsonProperty("refresh_token") // Specifies the name of the JSON property
    private String refreshToken; // JWT token used for refreshing access token

}
