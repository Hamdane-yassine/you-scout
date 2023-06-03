package ma.ac.inpt.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the response for updating a profile picture.
 * Contains the username and the URL of the updated profile picture.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfilePictureUpdateResponse {
    private String username;             // The username of the user
    private String profilePictureUrl;    // The URL of the updated profile picture
}



