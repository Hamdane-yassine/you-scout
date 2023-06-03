package inpt.ac.ma.chatservice.service;

import inpt.ac.ma.chatservice.exception.ResourceNotFoundException;
import inpt.ac.ma.chatservice.model.Chat;
import inpt.ac.ma.chatservice.model.MessageStatus;
import inpt.ac.ma.chatservice.repo.ChatRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepo repository;
    private final RoomService chatRoomService;

    public Chat save(Chat chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        return repository.save(chatMessage).block(); //block for it to become blocking (like await/async in JS)
    }

    public Mono<Long> countNewMessages(String senderId, String recipientId) {
        return repository.countBySenderIdAndRecipientIdAndStatus(senderId, recipientId, MessageStatus.RECEIVED);
    }

    public Mono<List<Chat>> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatId(senderId, recipientId, false);

        return chatId.map(id -> repository.findByChatId(id)
                        .flatMapMany(Flux::fromIterable)
                        .collectList())
                .orElseGet(() -> Mono.just(Collections.emptyList()));
    }

    public Mono<Mono<Chat>> findById(String id) {
        return repository.findById(id).map(chatMessage -> {
            if(chatMessage.getStatus()!=MessageStatus.DELIVERED){
                chatMessage.setStatus(MessageStatus.DELIVERED);
            }
            return repository.save(chatMessage);
        }).switchIfEmpty(Mono.error(new ResourceNotFoundException("can't find message (" + id + ")")));
    }

}