package ma.ac.inpt.authservice.repository;

import ma.ac.inpt.authservice.model.Token;
import ma.ac.inpt.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository<T extends Token> extends JpaRepository<T, Long> {
    Optional<T> findByToken(String token);

    Optional<T> findByUser(User user);
}
