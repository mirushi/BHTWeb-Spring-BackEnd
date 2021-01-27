package com.bhtcnpm.website;

import com.bhtcnpm.website.miscellenous.PrettySqlFormat;
import com.p6spy.engine.spy.P6SpyOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebsiteApplication {

    public static void main(String[] args) {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(PrettySqlFormat.class.getName());
        SpringApplication.run(WebsiteApplication.class, args);
    }

}
