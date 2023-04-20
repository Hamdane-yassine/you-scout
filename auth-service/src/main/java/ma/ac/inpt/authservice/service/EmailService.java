package ma.ac.inpt.authservice.service;

import ma.ac.inpt.authservice.payload.EmailPayload;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    public void sendEmail(EmailPayload payload);

}
