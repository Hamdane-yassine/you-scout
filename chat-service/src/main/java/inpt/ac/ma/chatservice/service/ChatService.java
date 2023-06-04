package inpt.ac.ma.chatservice.service;

import inpt.ac.ma.chatservice.model.Chat;
import inpt.ac.ma.chatservice.payload.ChatRequest;
import inpt.ac.ma.chatservice.payload.MessageStatus;
import inpt.ac.ma.chatservice.repo.ChatRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepo repository;
    private final RoomService chatRoomService;

    public Chat save(ChatRequest chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        return repository.save(convertTo(chatMessage));

    }

    public Long countNewMessages(String senderName, String recipientName) {
        return repository.countBySenderNameAndRecipientNameAndStatus(senderName, recipientName, MessageStatus.RECEIVED);
    }

    public List<Chat> findChatMessages(String senderName, String recipientName) {
        System.out.println("ayoub");
        String chatId = chatRoomService.getChatId(senderName, recipientName, true);


        return repository.findByChatId(chatId);

    }

    public Chat findById(String id) {
        return repository.findById(id).map(chatMessage -> {
            if(chatMessage.getStatus()!=MessageStatus.DELIVERED){
                chatMessage.setStatus(MessageStatus.DELIVERED);
            }
            return repository.save(chatMessage);
        }).orElse(null);
    }

    private Chat convertTo(ChatRequest chat) {
        return Chat
                .builder()
                .chatId(chat.getId())
                .recipientName(chat.getRecipientName())
                .senderName(chat.getSenderName())
                .content(chat.getContent())
                .timestamp(Date.from(Instant.now()))
                .status(chat.getStatus())
                .build();
    }

}