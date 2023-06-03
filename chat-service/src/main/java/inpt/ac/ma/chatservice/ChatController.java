package inpt.ac.ma.chatservice;


import inpt.ac.ma.chatservice.model.Chat;
import inpt.ac.ma.chatservice.model.Notification;
import inpt.ac.ma.chatservice.service.ChatService;
import inpt.ac.ma.chatservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatMessageService;
    private final RoomService chatRoomService;

    @MessageMapping("/sockjs/chat")
    public void processMessage(@Payload Chat chatMessage) {
        var chatId = chatRoomService.getChatId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.get());

        messagingTemplate.convertAndSendToUser(chatMessage.getRecipientId(), "/queue/messages", chatMessage);
        Chat saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(chatMessage.getRecipientId(), "/queue/messages/notification", new Notification(saved.getId(), saved.getSenderId(), saved.getSenderName()));
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public Mono<ResponseEntity<Long>> countNewMessages(@PathVariable String senderId, @PathVariable String recipientId) {

     return chatMessageService.countNewMessages(senderId, recipientId)
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "No new messages found")));
    }


    @GetMapping("/messages/{senderId}/{recipientId}")
    public Mono<ResponseEntity<List<Chat>>> findChatMessages(@PathVariable String senderId, @PathVariable String recipientId) {
        return chatMessageService.findChatMessages(senderId, recipientId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "No new messages found")));

    }

    @GetMapping("/messages/{id}")
    public Mono<ResponseEntity<Mono<Chat>>> findMessage(@PathVariable String id) {
        return chatMessageService.findById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "No new messages found")));

    }
}