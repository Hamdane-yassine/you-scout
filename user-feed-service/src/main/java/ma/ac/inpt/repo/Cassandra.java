package ma.ac.inpt.repo;

import ma.ac.inpt.models.UserFeedEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;

/**
 * The Cassandra interface provides methods for interacting with the Cassandra database
 * and extends the CrudRepository interface.
 */
public interface Cassandra extends CrudRepository<UserFeedEntity, String> {

    // Retrieves a slice of UserFeedEntity objects by username and pageable parameters
    Slice<UserFeedEntity> findByUsername(String username, Pageable pageable);



}