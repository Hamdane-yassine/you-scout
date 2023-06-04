package inpt.ac.ma.chatservice.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chat {

    private String id;

    private String recipientName;

    private String senderName;


    private String content;

    private Date timestamp;

    private MessageStatus status;
}
