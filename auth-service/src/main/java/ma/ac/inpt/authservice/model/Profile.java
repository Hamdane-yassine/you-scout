package ma.ac.inpt.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String fullName;

    private String profilePicture;

    private LocalDate dateOfBirth;

    @Size(max = 10)
    private String gender;

    @Size(max = 50)
    private String country;

    @Size(max = 50)
    private String cityOrRegion;

    @Size(max = 500)
    private String bio;

    // Map to store social media links
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Map<String, String> socialMediaLinks = new HashMap<>();

    // One-to-One relationship with the User entity
    @OneToOne(mappedBy = "profile")
    private User user;
}

