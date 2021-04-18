package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.config.email.EmailMessage;
import com.bhtcnpm.website.model.dto.EmailTemplate;
import com.bhtcnpm.website.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

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
    public void sendConfirmationEmail(String targetEmail) {
        //Create a thread-safe copy of the template message and customize it.
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setTo(targetEmail);
        msg.setText(MessageFormat.format(
                emailConfirmationTemplate.getContent(), "http://google.com"
        ));
        msg.setSubject(emailConfirmationTemplate.getSubject());

        try {
            this.mailSender.send(msg);
        } catch (MailException ex) {
            log.error(ex.getMessage());
        }
    }
}
