package com.bhtcnpm.website.model.exception;

public class CaptchaServerErrorException extends Exception {
    public CaptchaServerErrorException() {
        super();
    }
    public CaptchaServerErrorException (String message) {
        super(message);
    }
}
