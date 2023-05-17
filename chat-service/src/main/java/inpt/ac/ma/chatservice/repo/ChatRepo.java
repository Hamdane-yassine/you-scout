package inpt.ac.ma.chatservice.repo;


import inpt.ac.ma.chatservice.model.Chat;
import inpt.ac.ma.chatservice.model.MessageStatus;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface ChatRepo extends ReactiveCouchbaseRepository<Chat, String> {
    Mono<Long> countBySenderIdAndRecipientIdAndStatus(String senderId, String recipientId, MessageStatus status);

    Mono<List<Chat>> findByChatId(String chatId);
}



