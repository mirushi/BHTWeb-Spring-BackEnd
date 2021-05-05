package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.exception.CaptchaInvalidException;
import com.bhtcnpm.website.model.exception.CaptchaServerErrorException;

public interface CaptchaService {
    boolean verifyCaptcha (String captchaToken, String userIPAddress) throws CaptchaServerErrorException, CaptchaInvalidException;
}
