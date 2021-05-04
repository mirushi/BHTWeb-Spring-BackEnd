package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.constant.api.google.captcha.CaptchaErrorMessage;
import com.bhtcnpm.website.model.dto.Google.CaptchaRequestDTO;
import com.bhtcnpm.website.model.dto.Google.CaptchaResponseDTO;
import com.bhtcnpm.website.model.exception.CaptchaInvalidException;
import com.bhtcnpm.website.model.exception.CaptchaServerErrorException;
import com.bhtcnpm.website.service.CaptchaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class CaptchaServiceImpl implements CaptchaService {

    @Value("${com.google.recaptcha.secret.key}")
    private String G_CAPTCHA_SECRET_KEY;

    @Value("${com.google.recaptcha.verify.url}")
    private String G_CAPTCHA_VERIFY_URL;

    private final RestTemplate restTemplate;

    public boolean verifyCaptcha (String captchaToken, String userIPAddress) throws CaptchaServerErrorException, CaptchaInvalidException {
        CaptchaRequestDTO request = CaptchaRequestDTO.builder()
                .remoteip(userIPAddress)
                .response(captchaToken)
                .secret(G_CAPTCHA_SECRET_KEY)
                .build();

        HttpEntity<CaptchaRequestDTO> requestBody = new HttpEntity<>(request);

        ResponseEntity<CaptchaResponseDTO> response = restTemplate
                .postForEntity(G_CAPTCHA_VERIFY_URL, requestBody, CaptchaResponseDTO.class);

        if (HttpStatus.OK != response.getStatusCode()) {
            throw new CaptchaServerErrorException(CaptchaErrorMessage.CAPTCHA_SERVER_ERROR);
        }

        if (Boolean.TRUE != response.getBody().getSuccess()) {
            throw new CaptchaInvalidException(CaptchaErrorMessage.CAPTCHA_INVALID);
        }

        return true;
    }
}
