package ma.ac.inpt.authservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a user's profile.
 * Includes basic information about the user, such as their name, profile picture, date of birth, gender, and location,
 * as well as additional details such as a bio and links to their social media profiles.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {

    /**
     * The unique ID of the profile.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    /**
     * The full name of the user associated with the profile.
     */
    private String fullName;

    /**
     * The URL of the profile picture for the user.
     */
    private String profilePicture;

    /**
     * The date of birth of the user associated with the profile.
     */
    private LocalDate dateOfBirth;

    /**
     * The gender of the user associated with the profile.
     */
    private String gender;

    /**
     * The country where the user associated with the profile is located.
     */
    private String country;

    /**
     * The city or region where the user associated with the profile is located.
     */
    private String cityOrRegion;

    /**
     * A brief bio or description of the user associated with the profile.
     */
    private String bio;

    /**
     * A map of social media links associated with the user, keyed by the name of the social media platform.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Map<String, String> socialMediaLinks = new HashMap<>();

    /**
     * The User object associated with the profile.
     */
    @OneToOne(mappedBy = "profile")
    private User user;
}


