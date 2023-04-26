package ma.ac.inpt.authservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * A request payload for adding a new role to a user.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleRequest {

    /**
     * The username of the user to whom the new role will be added.
     */
    @NotNull
    @NotEmpty
    private String username;

    /**
     * The name of the role to be added to the user.
     */
    @NotNull
    @NotEmpty
    private String roleName;

}
