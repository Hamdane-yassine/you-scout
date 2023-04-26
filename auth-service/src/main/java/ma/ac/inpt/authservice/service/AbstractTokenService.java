package ma.ac.inpt.authservice.service;

import ma.ac.inpt.authservice.exception.InvalidRequestException;
import ma.ac.inpt.authservice.model.Token;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.payload.EmailPayload;
import ma.ac.inpt.authservice.repository.TokenRepository;

import java.util.Optional;

public abstract class AbstractTokenService<T extends Token> {

    protected abstract EmailService getEmailService();

    protected abstract TokenRepository<T> getTokenRepository();

    protected abstract User getUserFromToken(T token);

    protected abstract String getTokenContent(T token);

    public String sendTokenEmail(User user, String subject, String message) {
        Optional<T> tokenOptional = getTokenRepository().findByUser(user);
        T token;
        String responseMessage;
        if (tokenOptional.isPresent() && !isTokenExpired(tokenOptional.get())) {
            token = tokenOptional.get();
            responseMessage = message + " has already been sent to " + token.getUser().getEmail() + ". Please check your inbox and follow the instructions.";
        } else {
            token = createOrUpdateToken(user);
            String recipientAddress = token.getUser().getEmail();
            String confirmationLink = getTokenContent(token);
            String content = "Please click on the following link to proceed:<br>" + confirmationLink + "<br> This link will expire in 24 hours.";
            responseMessage = message + " has been sent to " + recipientAddress + ". Please check your inbox and follow the instructions.";
            getEmailService().sendEmail(EmailPayload.builder().recipientAddress(recipientAddress).subject(subject).content(content).build());
        }
        return responseMessage;
    }

    public String verifyToken(String tokenString, String value) {
        var token = getTokenRepository().findByToken(tokenString).orElseThrow(() -> new InvalidRequestException("Invalid Token"));
        if (isTokenExpired(token)) {
            return "Your token has expired. Please request a new one.";
        }
        User user = getUserFromToken(token);
        handleValidToken(user, value);
        getTokenRepository().delete(token);

        return "Your operation has been successfully completed!";
    }

    protected abstract void handleValidToken(User user, String value);

    private T createOrUpdateToken(User user) {
        Optional<T> tokenOptional = getTokenRepository().findByUser(user);
        T token;
        token = tokenOptional.map(this::updateToken).orElseGet(() -> createToken(user));
        return token;
    }

    protected abstract T createToken(User user);

    protected abstract T updateToken(T token);

    protected abstract boolean isTokenExpired(T token);

}

