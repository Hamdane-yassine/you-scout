package inpt.ac.ma.chatservice;

import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface CouchBaseRepo extends ReactiveCouchbaseRepository<Person, String> {

    Flux<Person> findByLastName(String lastName);
}