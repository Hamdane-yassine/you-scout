package ma.ac.inpt.authservice.repository;

import ma.ac.inpt.authservice.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    boolean existsByTokenUuid(String tokenUuid);
}
