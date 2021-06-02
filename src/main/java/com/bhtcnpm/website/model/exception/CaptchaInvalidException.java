package com.bhtcnpm.website.model.exception;

public class CaptchaInvalidException extends Exception {
    public CaptchaInvalidException () {super();}
    public CaptchaInvalidException (String message) {super(message);}
}
