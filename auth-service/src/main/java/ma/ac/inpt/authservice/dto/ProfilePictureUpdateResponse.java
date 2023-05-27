package ma.ac.inpt.authservice.dto;

import lombok.Data;

@Data
public class ProfilePictureUpdateResponse {
    private String username;
    private String profilePictureUrl;
}

