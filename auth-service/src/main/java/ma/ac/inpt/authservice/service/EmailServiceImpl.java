package ma.ac.inpt.authservice.service;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.authservice.exception.EmailSendingException;
import ma.ac.inpt.authservice.exception.EmailTemplateLoadingException;
import ma.ac.inpt.authservice.payload.EmailPayload;
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
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${email.template.path}")
    private String templatePath;

    @Value("${spring.mail.from}")
    private String sender;

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
            String htmlContent = getHtmlContent(payload.getSubject(),payload.getContent());
            helper.setText(htmlContent, true);
        } catch (MessagingException e) {
            throw new EmailSendingException("error while sending email");
        }
        javaMailSender.send(email);
    }

    private String getHtmlContent(String title, String content) {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            Resource resource = new ClassPathResource(templatePath);
            File file = resource.getFile();
            String htmlContent = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
            htmlContent = htmlContent.replace("{{title}}", title);
            htmlContent = htmlContent.replace("{{content}}", content);
            return htmlContent;
        } catch (IOException e) {
            throw new EmailTemplateLoadingException("Error loading email template");
        }
    }



}
