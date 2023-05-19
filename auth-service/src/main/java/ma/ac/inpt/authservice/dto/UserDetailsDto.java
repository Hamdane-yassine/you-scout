package ma.ac.inpt.authservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ac.inpt.authservice.util.UserDetailsDtoSerializer;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

/**
 * Represents a DTO (Data Transfer Object) for the user details.
 * This class contains information such as the user's email, username, full name, profile picture, date of birth, gender, country,
 * city/region, bio, social media links, whether the user is enabled, and the roles assigned to the user.
 * This DTO is used to transfer user details from the server to the client.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = UserDetailsDtoSerializer.class)
public class UserDetailsDto {

    /**
     * The email address of the user.
     */
    private String email;
    /**
     * The username of the user.
     */
    private String username;
    /**
     * The password of the user.
     */
    @JsonIgnore
    private String password;
    /**
     * The full name of the user.
     */
    private String fullName;
    /**
     * The profile picture of the user.
     */
    private String profilePicture;
    /**
     * The date of birth of the user.
     */
    private LocalDate dateOfBirth;
    /**
     * The gender of the user.
     */
    private String gender;
    /**
     * The country of the user.
     */
    private String country;
    /**
     * The city or region of the user.
     */
    private String cityOrRegion;
    /**
     * The bio of the user.
     */
    private String bio;
    /**
     * A map containing the social media links of the user.
     */
    private Map<String, String> socialMediaLinks;
    /**
     * A boolean indicating whether the user is enabled.
     */
    private boolean isEnabled;
    /**
     * A collection of roles assigned to the user.
     */
    private Collection<String> roles;
}

