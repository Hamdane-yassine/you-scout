package ma.ac.inpt.authservice.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserUpdateResponse {

    private String email;
    private String username;

}
