package ma.ac.inpt.authservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Represents a user role request containing the username of the user and the name of the role to assign or remove.
 * This class is used to request the assignment or removal of a role for a user.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleRequest {

    /**
     * The username of the user.
     * It must be between 1 and 50 characters long.
     */
    @NotNull(message = "Username cannot be null.")
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters long.")
    private String username;
    /**
     * The name of the role to assign or remove.
     * It cannot be empty.
     */
    @NotNull(message = "Role name cannot be null.")
    @NotEmpty(message = "Role name cannot be empty.")
    private String roleName;
}
