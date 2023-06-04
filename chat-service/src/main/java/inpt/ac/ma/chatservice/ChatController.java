package inpt.ac.ma.chatservice;


import inpt.ac.ma.chatservice.model.Chat;
import inpt.ac.ma.chatservice.payload.ChatRequest;
import inpt.ac.ma.chatservice.payload.Notification;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatMessageService;
    private final RoomService chatRoomService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatRequest chatMessage) {
        String convId = chatRoomService.getChatId(chatMessage.getSenderName(), chatMessage.getRecipientName(), true);
        if(convId!=null){
            chatMessage.setConvId(convId);
            messagingTemplate.convertAndSendToUser(chatMessage.getRecipientName(), "/queue/messages", chatMessage);
            chatMessageService.save(chatMessage);
//            messagingTemplate.convertAndSendToUser(chatMessage.getRecipientName(), "/queue/messages/notification", new Notification(saved.getId(), saved.getSenderName()));

        }
    }

    @GetMapping("/messages/{senderName}/{recipientName}/count")
    public ResponseEntity<Long> countNewMessages(@PathVariable String senderName, @PathVariable String recipientName) {

            Long count = chatMessageService.countNewMessages(senderName, recipientName);
            if (count != null) {
                return ResponseEntity.ok(count);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No new messages found");
            }
    }


    @GetMapping("/messages/{senderName}/{recipientName}")
    public ResponseEntity<List<Chat>> findChatMessages(@PathVariable String senderName, @PathVariable String recipientName) {

        List<Chat> msgList = chatMessageService.findChatMessages(senderName, recipientName);
        if (msgList != null) {
            return ResponseEntity.ok(msgList);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No new messages found");
        }
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<Chat> findMessage(@PathVariable String id) {

        Chat msg = chatMessageService.findById(id);
        if (msg != null) {
            return ResponseEntity.ok(msg);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No corresponding message");
        }
    }
}