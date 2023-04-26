package ma.ac.inpt.authservice.service;

import ma.ac.inpt.authservice.payload.EmailPayload;

public interface EmailService {

    void sendEmail(EmailPayload payload);

}
