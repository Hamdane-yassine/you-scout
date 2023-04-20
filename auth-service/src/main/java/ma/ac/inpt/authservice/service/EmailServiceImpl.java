package ma.ac.inpt.authservice.service;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.authservice.payload.EmailPayload;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Async
    @Override
    public void sendEmail(EmailPayload payload) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(payload.getRecipientAddress());
        email.setSubject(payload.getSubject());
        email.setText(payload.getContent());
        email.setFrom(payload.getSender());
        javaMailSender.send(email);
    }
}
