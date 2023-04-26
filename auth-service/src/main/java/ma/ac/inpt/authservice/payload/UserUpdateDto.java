package ma.ac.inpt.authservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ac.inpt.authservice.util.ValidAge;

import javax.annotation.Nullable;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

    @Nullable
    @Email
    private String email;

    @Nullable
    @Size(min = 1,max = 50)
    private String username;

    @Nullable
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?_&])[A-Za-z\\d@$!%*?_&]+$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;

    @Nullable
    @Size(min = 1,max = 50)
    private String fullName;

    @Nullable
    @ValidAge
    private LocalDate dateOfBirth;

    @Nullable
    @Size(min = 1,max = 10)
    private String gender;

    @Nullable
    @Size(min = 1,max = 50)
    private String country;

    @Nullable
    @Size(min = 1,max = 50)
    private String cityOrRegion;

    @Nullable
    @Size(min = 1,max = 500)
    private String bio;

    @Nullable
    private Map<@Size(min = 1,max = 30) String, @Size(min = 1,max = 200) String> socialMediaLinks;
}

