package com.bhtcnpm.website.service;

public interface EmailService {
    void sendConfirmationEmail (String targetEmail, String token);
    void sendForgotPasswordEmail (String targetEmail, String token);
}
