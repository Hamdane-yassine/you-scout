package inpt.ac.ma.chatservice.service;

import inpt.ac.ma.chatservice.model.Room;
import inpt.ac.ma.chatservice.repo.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepo chatRoomRepository;

    public String getChatId(String senderName, String recipientName, boolean createIfNotExist) {
        String chatId = chatRoomRepository.findBySenderNameAndRecipientName(senderName, recipientName).getChatId();

        if (chatId != null) {
            return chatId;
        }

        if (!createIfNotExist) {
            return null;
        }

        chatId = String.format("%s_%s", senderName, recipientName);
        Room senderRecipient = Room.builder().chatId(chatId).senderName(senderName).recipientName(recipientName).build();
        Room recipientSender = Room.builder().chatId(chatId).senderName(recipientName).recipientName(senderName).build();
        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }
}