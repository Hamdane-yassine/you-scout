package ma.ac.inpt.repo;

import ma.ac.inpt.UserFeedEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;


public interface Cassandra extends CrudRepository<UserFeedEntity, String> {

    Slice<UserFeedEntity> findByUsername(String username, Pageable pageable);


}