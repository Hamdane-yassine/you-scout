package ma.ac.inpt.authservice.repository;

import ma.ac.inpt.authservice.model.Token;
import ma.ac.inpt.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing Token entities.
 *
 * @param <T> the type of token managed by this repository
 */
public interface TokenRepository<T extends Token> extends JpaRepository<T, Long> {

    /**
     * Returns an Optional containing the Token object with the specified token value, or an empty Optional if not found.
     *
     * @param token the token value to search for
     * @return an Optional containing the Token object with the specified token value, or an empty Optional if not found
     */
    Optional<T> findByToken(String token);

    /**
     * Returns an Optional containing the Token object associated with the specified User object, or an empty Optional if not found.
     *
     * @param user the User object to search for
     * @return an Optional containing the Token object associated with the specified User object, or an empty Optional if not found
     */
    Optional<T> findByUser(User user);
}

