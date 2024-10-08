package inpt.ac.ma.chatservice.repo;

import inpt.ac.ma.chatservice.model.Room;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.CrudRepository;

public interface RoomRepo extends CrudRepository<Room, String> {

    @AllowFiltering
    Room findBySenderNameAndRecipientName(String senderName, String recipientName);




}