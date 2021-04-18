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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@Slf4j
public class EmailMessage {
    private static final String PATH_TO_EMAIL_TEMPLATE_CONFIG = "documents/emails/emails.properties";

    private Map<String, EmailTemplate> emailTemplates;

    private Properties props;

    @PostConstruct
    public void postConstruct () throws IOException {
        emailTemplates = new HashMap<>();
        Resource resource = new ClassPathResource(PATH_TO_EMAIL_TEMPLATE_CONFIG);
        props = PropertiesLoaderUtils.loadProperties(resource);

        addEmailTemplate("confirmRegistration");
    }

    public EmailTemplate getEmailTemplateByCode (String code) {
        return emailTemplates.get(code);
    }

    private void addEmailTemplate (String templateName) throws IOException {
        String emailCode = props.getProperty(templateName + ".emailCode");
        String title = props.getProperty(templateName + ".title");
        String pathToEmailTemplateText = props.getProperty(templateName + ".content_text");
        String pathToEmailTemplateHTML = props.getProperty(templateName + ".content_html");

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

        emailTemplates.put(emailCode + ".text", emailTemplateText);
        emailTemplates.put(emailCode + ".html", emailTemplateHTML);
    }
}
