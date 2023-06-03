package ma.ac.inpt.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileUpdateResponse {

    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String country;
    private String cityOrRegion;
    private String bio;
    private Map<String,String> socialMediaLinks;
}
