package inpt.ac.ma.chatservice.service;

import inpt.ac.ma.chatservice.model.Room;
import inpt.ac.ma.chatservice.repo.RoomRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepo chatRoomRepository;

    public Optional<String> getChatId(String senderId, String recipientId, boolean createIfNotExist) {

        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId).map(Room::getChatId).or(() -> {
            if (!createIfNotExist) {
                return Optional.empty();
            }
            var chatId = String.format("%s_%s", senderId, recipientId);
            Room senderRecipient = Room.builder().chatId(chatId).senderId(senderId).recipientId(recipientId).build();
            Room recipientSender = Room.builder().chatId(chatId).senderId(recipientId).recipientId(senderId).build();
            chatRoomRepository.save(senderRecipient);
            chatRoomRepository.save(recipientSender);
            return Optional.of(chatId);
        });
    }
}