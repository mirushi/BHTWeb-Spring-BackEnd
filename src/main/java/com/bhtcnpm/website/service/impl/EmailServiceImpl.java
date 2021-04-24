package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.config.email.EmailMessageService;
import com.bhtcnpm.website.config.email.EmailMessageTemplate;
import com.bhtcnpm.website.config.email.EmailMessageTemplateType;
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
    //TODO: HTTP is not secure. This is for development only. Use HTTPS.
    private static final String EMAIL_VERIFICATION_URL = "http://{0}/verify?email={1}&token={2}";
    private static final String FORGOT_PASSWORD_URL = "http://{0}/forgot-password/verify?email={1}&token={2}";

    @Value("${website.domain.frontend}")
    private String FRONT_END_DOMAIN;

    private final JavaMailSender mailSender;
    private final SimpleMailMessage templateMessage;

    private final EmailMessageService emailMessageService;
    private EmailTemplate emailConfirmationTemplate;
    private EmailTemplate forgotPasswordTemplate;

    @PostConstruct
    public void postConstruct () {
        emailConfirmationTemplate =
                emailMessageService.getEmailTemplateByCode(EmailMessageTemplate.EMAIL_CONFIRMATION, EmailMessageTemplateType.TEXT);
        forgotPasswordTemplate =
                emailMessageService.getEmailTemplateByCode(EmailMessageTemplate.FORGOT_PASSWORD, EmailMessageTemplateType.TEXT);
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

    @Override
    @Async
    public void sendForgotPasswordEmail(String targetEmail, String token) {
        //Create a thread-safe copy of the template message and customize it.
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
        msg.setTo(targetEmail);
        msg.setText(MessageFormat.format(
            forgotPasswordTemplate.getContent(),
            getForgotPasswordVerificationURL(targetEmail, token)
        ));
        msg.setSubject(forgotPasswordTemplate.getSubject());

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

    private String getForgotPasswordVerificationURL (String targetEmail, String token) {
        //TODO: HTTP is not secure. This is for development only. Use HTTPS.
        String result = MessageFormat.format(FORGOT_PASSWORD_URL, FRONT_END_DOMAIN, targetEmail, token);
        return result;
    }
}
