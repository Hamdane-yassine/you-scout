package ma.ac.inpt.authservice.payload;

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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = UserDetailsDtoSerializer.class)
public class UserDetailsDto {

    private String email;

    private String username;

    @JsonIgnore
    private String password;

    private String fullName;

    private String profilePicture;
    private LocalDate dateOfBirth;

    private String gender;

    private String country;

    private String cityOrRegion;

    private String bio;

    private Map<String, String> socialMediaLinks;

    private boolean isEnabled;

    private Collection<String> roles;
}

