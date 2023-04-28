package ma.ac.inpt.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * The Role entity represents the different roles available in the application.
 * This entity is used to map the "roles" table in the database using JPA annotations.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    /**
     * The unique identifier of the role.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the role. This field is marked as unique to ensure that there are no
     * duplicate roles in the database.
     */
    @Column(unique = true)
    private String roleName;
}


