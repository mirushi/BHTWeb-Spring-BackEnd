package com.bhtcnpm.website.config.email;

import com.bhtcnpm.website.model.dto.EmailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StreamUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@Slf4j
public class EmailMessageService {
    private static final String PATH_TO_EMAIL_TEMPLATE_CONFIG = "documents/emails/emails.properties";

    private Map<EmailMessageTemplate, EmailTemplate> emailTextTemplates;

    private Map<EmailMessageTemplate, EmailTemplate> emailHTMLTemplates;

    private Properties props;

    @PostConstruct
    public void postConstruct () throws IOException {
        emailTextTemplates = new EnumMap<>(EmailMessageTemplate.class);
        emailHTMLTemplates = new EnumMap<>(EmailMessageTemplate.class);

        Resource resource = new ClassPathResource(PATH_TO_EMAIL_TEMPLATE_CONFIG);
        props = PropertiesLoaderUtils.loadProperties(resource);

        //Add all email templates to Map.
        for (EmailMessageTemplate template : EmailMessageTemplate.values()) {
            addEmailTemplate(template);
        }
    }

    public EmailTemplate getEmailTemplateByCode (EmailMessageTemplate code, EmailMessageTemplateType type) {
        Map<EmailMessageTemplate, EmailTemplate> selectedEmailTemplates = null;
        if (EmailMessageTemplateType.TEXT == type) {
            selectedEmailTemplates = this.emailTextTemplates;
        } else if (EmailMessageTemplateType.HTML == type) {
            selectedEmailTemplates = this.emailHTMLTemplates;
        } else {
            throw new IllegalArgumentException("Email message template type is not supported.");
        }

        return selectedEmailTemplates.get(code);
    }

    private void addEmailTemplate (EmailMessageTemplate template) throws IOException {
        String emailCode = template.toString();
        String title = props.getProperty(emailCode + ".title");
        String pathToEmailTemplateText = props.getProperty(emailCode + ".content_text");
        String pathToEmailTemplateHTML = props.getProperty(emailCode + ".content_html");

        Resource emailContentTextResource =
                new ClassPathResource("documents/emails/text/" + pathToEmailTemplateText);
        Resource emailContentHTMLResource =
                new ClassPathResource("documents/emails/html/" + pathToEmailTemplateHTML);

        String emailContentText;
        String emailContentHTML;

        try (InputStream in = emailContentTextResource.getInputStream()) {
            emailContentText = StreamUtils.copyToString(in, Charset.defaultCharset());
        }
        try (InputStream in = emailContentHTMLResource.getInputStream()) {
            emailContentHTML = StreamUtils.copyToString(in, Charset.defaultCharset());
        }

        EmailTemplate emailTemplateText = EmailTemplate.builder()
                .subject(title)
                .content(emailContentText)
                .build();

        EmailTemplate emailTemplateHTML = EmailTemplate.builder()
                .subject(title)
                .content(emailContentHTML)
                .build();

        emailTextTemplates.put(template, emailTemplateText);
        emailHTMLTemplates.put(template, emailTemplateHTML);
    }
}
