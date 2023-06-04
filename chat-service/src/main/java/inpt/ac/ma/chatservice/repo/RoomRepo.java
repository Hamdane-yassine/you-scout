package inpt.ac.ma.chatservice.repo;

import org.springframework.data.repository.CrudRepository;

public interface CRoomRepo extends CrudRepository<Room, String> {

    Room findBySenderIdAndRecipientId(String senderId, String recipientId);




}