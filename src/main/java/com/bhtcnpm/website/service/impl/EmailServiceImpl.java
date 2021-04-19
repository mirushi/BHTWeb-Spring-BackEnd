package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.config.email.EmailMessage;
import com.bhtcnpm.website.model.dto.EmailTemplate;
import com.bhtcnpm.website.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private static final String EMAIL_VERIFICATION_URL = "http://{0}/verify?email={1}&token={2}";

    @Value("${website.domain.frontend}")
    private String FRONT_END_DOMAIN;

    private final JavaMailSender mailSender;
    private final SimpleMailMessage templateMessage;

    private final EmailMessage emailMessage;
    private EmailTemplate emailConfirmationTemplate;

    @PostConstruct
    public void postConstruct () {
        emailConfirmationTemplate =
                emailMessage.getEmailTemplateByCode("emailConfirmationMessage.text");
    }

    @Override
    @Async
    public void sendConfirmationEmail(String targetEmail, String token) {
        //Create a thread-safe copy of the template message and customize it.
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setTo(targetEmail);
        msg.setText(MessageFormat.format(
                emailConfirmationTemplate.getContent(),
                token,
                getEmailVerificationURL(targetEmail, token)
        ));
        msg.setSubject(emailConfirmationTemplate.getSubject());

        try {
            this.mailSender.send(msg);
        } catch (MailException ex) {
            log.error(ex.getMessage());
        }
    }

    private String getEmailVerificationURL(String targetEmail, String token) {
        //TODO: HTTP is not secure. This is for development only. Use HTTPS.
        String result = MessageFormat.format(EMAIL_VERIFICATION_URL, FRONT_END_DOMAIN, targetEmail, token);
        return result;
    }
}
