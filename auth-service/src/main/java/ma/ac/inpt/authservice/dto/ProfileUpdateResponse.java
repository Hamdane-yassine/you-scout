package ma.ac.inpt.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

/**
 * Represents the response for updating a user profile.
 * Contains the updated user profile information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateResponse {
    private String fullName;                     // The full name of the user
    private LocalDate dateOfBirth;               // The date of birth of the user
    private String gender;                       // The gender of the user
    private String country;                      // The country of the user
    private String cityOrRegion;                 // The city or region of the user
    private String bio;                          // The bio or description of the user
    private Map<String, String> socialMediaLinks; // The social media links of the user
}
