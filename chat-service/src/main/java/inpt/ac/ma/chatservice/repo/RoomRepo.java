package inpt.ac.ma.chatservice.repo;

import inpt.ac.ma.chatservice.model.Room;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;

import java.util.Optional;

public interface RoomRepo extends ReactiveCouchbaseRepository<Room, String> {
    Optional<Room> findBySenderIdAndRecipientId(String senderId, String recipientId);
}
