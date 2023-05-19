package ma.ac.inpt.authservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Represents a User entity in the application.
 * Implements UserDetails interface from Spring Security to provide user authentication and authorization functionality.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Email address of the user.
     */
    @Email
    private String email;

    /**
     * Hashed password of the user.
     */
    @Size(min = 8)
    private String password;

    /**
     * Username of the user.
     */
    @Size(min = 1, max = 50)
    private String username;

    /**
     * Indicates if the user's account is enabled.
     */
    private boolean isEnabled;

    /**
     * Collection of roles assigned to the user.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();

    /**
     * Profile associated with the user.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private Profile profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private RefreshToken refreshToken;

    /**
     * Returns a collection of GrantedAuthority objects representing the roles assigned to the user.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
    }

    /**
     * Returns the user's hashed password.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the user's username.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Returns true if the user's account is not expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Returns true if the user's account is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Returns true if the user's credentials are not expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Returns true if the user's account is enabled.
     */
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}

