package ma.ac.inpt.authservice.repository;

import ma.ac.inpt.authservice.model.RefreshToken;
import ma.ac.inpt.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    void deleteByUser(User user);

    boolean existsByTokenUuid(String tokenUuid);
}
