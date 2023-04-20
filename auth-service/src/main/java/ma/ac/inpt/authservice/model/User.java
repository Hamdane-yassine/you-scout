package ma.ac.inpt.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* First name of the user */
    private String firstname;

    /* Last name of the user */
    private String lastname;

    /* Email address of the user */
    private String email;

    /* Password of the user */
    private String password;

    /* Username of the user */
    private String username;

    /* whether the user's account is enabled or not */
    private boolean isEnabled;

    /* Roles of the user */
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();

    /* Returns a list of the user's granted authorities based on their role */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
    }

    /* Returns the user's password */
    @Override
    public String getPassword() {
        return password;
    }

    /* Returns the user's username */
    @Override
    public String getUsername() {
        return username;
    }

    /* Returns whether the user's account has expired */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /* Returns whether the user's account is locked */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /* Returns whether the user's credentials have expired */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /* Returns whether the user's account is enabled */
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
