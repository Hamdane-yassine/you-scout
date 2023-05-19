package ma.ac.inpt.authservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
public class ProfileUpdateResponse {

    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String country;
    private String cityOrRegion;
    private String bio;
    private Map<String,String> socialMediaLinks;
}
