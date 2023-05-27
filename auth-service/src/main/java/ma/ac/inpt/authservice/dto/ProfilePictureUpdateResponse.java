package ma.ac.inpt.authservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfilePictureUpdateResponse {
    private String username;
    private String profilePictureUrl;
}

