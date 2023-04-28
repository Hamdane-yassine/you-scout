package ma.ac.inpt.authservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ac.inpt.authservice.util.ValidAge;

import javax.annotation.Nullable;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Map;

/**
 * Represents a DTO (Data Transfer Object) for updating user details.
 * This class contains information such as the user's email, username, password, full name, date of birth, gender, country,
 * city/region, bio, and social media links.
 * This DTO is used to transfer updated user details from the client to the server.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

    /**
     * The email address of the user.
     * It must be a valid email format.
     */
    @Nullable
    @Email(message = "The email address is not valid.")
    private String email;

    /**
     * The username of the user.
     * It must be between 1 and 50 characters long.
     */
    @Nullable
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters long.")
    private String username;

    /**
     * The password of the user.
     * It must be at least 8 characters long and contain at least one uppercase letter,
     * one lowercase letter, one number, and one special character.
     */
    @Nullable
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$", message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character.")
    private String password;

    /**
     * The full name of the user.
     * It must be between 1 and 50 characters long.
     */
    @Nullable
    @Size(min = 1, max = 50, message = "Full name must be between 1 and 50 characters long.")
    private String fullName;

    /**
     * The date of birth of the user.
     * It must be a valid age.
     */
    @Nullable
    @ValidAge(message = "The date of birth is not valid.")
    private LocalDate dateOfBirth;

    /**
     * The gender of the user.
     * It must be between 1 and 10 characters long.
     */
    @Nullable
    @Size(min = 1, max = 10, message = "Gender must be between 1 and 10 characters long.")
    private String gender;

    /**
     * The country of the user.
     * It must be between 1 and 50 characters long.
     */
    @Nullable
    @Size(min = 1, max = 50, message = "Country must be between 1 and 50 characters long.")
    private String country;

    /**
     * The city or region of the user.
     * It must be between 1 and 50 characters long.
     */
    @Nullable
    @Size(min = 1, max = 50, message = "City/region must be between 1 and 50 characters long.")
    private String cityOrRegion;

    /**
     * The bio of the user.
     * It must be between 1 and 500 characters long.
     */
    @Nullable
    @Size(min = 1, max = 500, message = "Bio must be between 1 and 500 characters long.")
    private String bio;

    /**
     * A map containing the social media links of the user.
     * The keys must be between 1 and 30 characters long, and the values must be between 1 and 200 characters long.
     */
    @Nullable
    private Map<@Size(min = 1, max = 30, message = "Social media links keys must be between 1 and 30 characters long.") String, @Size(min = 1, max = 200, message = "Social media links values must be between 1 and 200 characters long.") String> socialMediaLinks;
}

