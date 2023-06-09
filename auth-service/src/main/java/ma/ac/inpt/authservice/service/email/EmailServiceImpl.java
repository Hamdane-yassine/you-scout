package ma.ac.inpt.authservice.service.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.authservice.exception.email.EmailSendingException;
import ma.ac.inpt.authservice.exception.email.EmailTemplateLoadingException;
import ma.ac.inpt.authservice.dto.EmailPayload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender; // Component for sending emails

    @Value("${email.template.path}")
    private String templatePath; // Path to the email template file

    @Value("${spring.mail.username}")
    private String sender; // Email address of the sender

    /**
     * Sends an email asynchronously using the provided email payload.
     *
     * @param payload the email payload containing the recipient address, subject, and content
     */
    @Async
    @Override
    public void sendEmail(EmailPayload payload) {
        MimeMessage email = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(email, true);
            helper.setTo(payload.getRecipientAddress());
            helper.setSubject(payload.getSubject());
            helper.setFrom(sender);
            String htmlContent = getHtmlContent(payload.getSubject(), payload.getContent());
            helper.setText(htmlContent, true);
            log.info("Sending email to: {}, with subject: {}", payload.getRecipientAddress(), payload.getSubject());
        } catch (MessagingException e) {
            log.error("Error while sending email to: {}, with subject: {}", payload.getRecipientAddress(), payload.getSubject(), e);
            throw new EmailSendingException("Error while sending email");
        }
        javaMailSender.send(email);
        log.info("Email sent successfully to: {}, with subject: {}", payload.getRecipientAddress(), payload.getSubject());
    }

    /**
     * Generates the HTML content for the email by replacing the title and content placeholders in the email template.
     *
     * @param title   the email title to be inserted into the template
     * @param content the email content to be inserted into the template
     * @return the generated HTML content for the email
     */
    private String getHtmlContent(String title, String content) {
        try {
            Resource resource = new ClassPathResource(templatePath);
            File file = resource.getFile();
            String htmlContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            htmlContent = htmlContent.replace("{{title}}", title);
            htmlContent = htmlContent.replace("{{content}}", content);
            log.info("Email template loaded successfully");
            return htmlContent;
        } catch (IOException e) {
            log.error("Error loading email template", e);
            throw new EmailTemplateLoadingException("Error loading email template");
        }
    }
}
