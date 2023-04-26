package ma.ac.inpt.authservice.service;

import ma.ac.inpt.authservice.model.User;

public interface AccountVerificationService {

    String sendVerificationEmail(User user);

    String verifyAccount(String token);
}
