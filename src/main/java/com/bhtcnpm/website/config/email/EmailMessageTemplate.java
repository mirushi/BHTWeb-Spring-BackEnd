package com.bhtcnpm.website.config.email;

public enum EmailMessageTemplate {
    //The String value corresponding to the emails.properties file.
    EMAIL_CONFIRMATION("confirmRegistration"), FORGOT_PASSWORD("forgotPassword");

    private final String text;

    EmailMessageTemplate(final String text){
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
