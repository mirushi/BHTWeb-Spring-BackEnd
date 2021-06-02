package com.bhtcnpm.website.config.email;

import com.querydsl.core.annotations.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

@Configuration
public class EmailConfig {
    @Bean
    public SimpleMailMessage getSimpleMailMessage () {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bht.cnpm.bot@gmail.com");

        return message;
    }
}
