package inpt.ac.ma.chatservice.repo;

import inpt.ac.ma.chatservice.model.Chat;
import inpt.ac.ma.chatservice.payload.MessageStatus;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatRepo extends CrudRepository<Chat, String> {
    @AllowFiltering
    Long countBySenderNameAndRecipientNameAndStatus(String senderName, String recipientName, MessageStatus status);

    List<Chat> findByChatId(String chatId);




}