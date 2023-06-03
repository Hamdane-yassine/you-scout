package ma.ac.inpt.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilePictureUpdateResponse {
    private String username;
    private String profilePictureUrl;
}


