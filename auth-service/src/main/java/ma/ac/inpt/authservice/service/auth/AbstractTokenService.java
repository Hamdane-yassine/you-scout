package ma.ac.inpt.authservice.service.auth;

import ma.ac.inpt.authservice.exception.registration.InvalidRequestException;
import ma.ac.inpt.authservice.model.Token;
import ma.ac.inpt.authservice.model.User;
import ma.ac.inpt.authservice.payload.EmailPayload;
import ma.ac.inpt.authservice.repository.TokenRepository;
import ma.ac.inpt.authservice.service.email.EmailService;

import java.util.Optional;

/**
 * Abstract base class for services that handle tokens.
 * <p>
 * Provides common functionality for sending and verifying tokens via email, and creating/updating/expiring tokens.
 *
 * @param <T> The type of token to be handled.
 */
public abstract class AbstractTokenService<T extends Token> {

    /**
     * Returns the EmailService implementation to be used by this token service.
     *
     * @return the EmailService implementation to be used by this token service
     */
    protected abstract EmailService getEmailService();

    /**
     * Returns the TokenRepository implementation to be used by this token service.
     *
     * @return the TokenRepository implementation to be used by this token service
     */
    protected abstract TokenRepository<T> getTokenRepository();

    /**
     * Returns the user associated with the specified token.
     *
     * @param token the token to get the user for
     * @return the user associated with the specified token
     */
    protected abstract User getUserFromToken(T token);

    /**
     * Returns the content of the specified token.
     *
     * @param token the token to get the content for
     * @return the content of the specified token
     */
    protected abstract String getTokenContent(T token);

    /**
     * Sends an email with a token to the specified user, if a token has not already been sent.
     * If a token has already been sent and has not expired, a message is returned indicating that the token has already been sent.
     *
     * @param user    the user to send the token to
     * @param subject the subject of the email containing the token
     * @param message the message to include in the email
     * @return a message indicating the status of the email and token sending process
     */
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

    /**
     * Verifies the specified token and performs some operation on the user associated with the token.
     *
     * @param tokenString the string representation of the token to verify
     * @param value       the value to pass to the handleValidToken method
     * @return a message indicating the status of the token verification process
     */
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

    /**
     * Performs some operation on the user associated with a valid token.
     *
     * @param user  the user associated with the valid token
     * @param value the value to use in the operation
     */

    protected abstract void handleValidToken(User user, String value);

    /**
     * Creates a new token for the specified user.
     *
     * @param user the user to create the token for
     * @return the newly created token
     */
    private T createOrUpdateToken(User user) {
        Optional<T> tokenOptional = getTokenRepository().findByUser(user);
        T token;
        token = tokenOptional.map(this::updateToken).orElseGet(() -> createToken(user));
        return token;
    }

    /**
     * Creates a new token for the specified user.
     *
     * @param user the user to create the token for
     * @return the newly created token
     */
    protected abstract T createToken(User user);

    /**
     * Updates an existing token.
     *
     * @param token the token to update
     * @return the updated token
     */
    protected abstract T updateToken(T token);

    /**
     * Returns true if the specified token is expired.
     *
     * @param token the token to check for expiration
     * @return true if the specified token is expired, false otherwise
     */
    protected abstract boolean isTokenExpired(T token);
}

