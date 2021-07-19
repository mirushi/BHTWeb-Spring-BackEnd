package com.bhtcnpm.website;

import org.hibernate.annotations.TypeDef;
import org.hibernate.type.PostgresUUIDType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.UUID;

@Configuration
public class WebsiteApplicationConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer () {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000", "http://uitstudy.space", "https://uitstudy.space:443", "https://uitstudy.space",
                                "http://www.uitstudy.space", "https://www.uitstudy.space:443", "https://www.uitstudy.space")
                        .allowedMethods("GET", "PUT", "POST", "DELETE")
                        .allowedHeaders("Authorization", "Cache-Control" ,"Content-Type");
            }
        };
    }
}
