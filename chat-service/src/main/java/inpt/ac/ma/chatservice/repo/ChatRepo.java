package inpt.ac.ma.chatservice.repo;

import inpt.ac.ma.chatservice.payload.MessageStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CChatRepo extends CrudRepository<Chat, String> {

    Long countBySenderIdAndRecipientIdAndStatus(String senderId, String recipientId, MessageStatus status);

    List<Chat> findByChatId(String chatId);




}