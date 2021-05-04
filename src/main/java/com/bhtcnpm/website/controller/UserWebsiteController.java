package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.UserWebsite.*;
import com.bhtcnpm.website.model.exception.CaptchaInvalidException;
import com.bhtcnpm.website.model.exception.CaptchaServerErrorException;
import com.bhtcnpm.website.service.UserWebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UserWebsiteController {

    private final UserWebsiteService userWebsiteService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<UserDetailsDTO> register (@RequestBody UserWebsiteCreateNewRequestDTO dto)
            throws CaptchaServerErrorException, CaptchaInvalidException {
        UserAuthenticatedDTO authenticatedDTO = userWebsiteService.createNewNormalUser(dto);

        return new ResponseEntity<>(authenticatedDTO.getUserDetailsDTO(), authenticatedDTO.getHeaders(), HttpStatus.OK);
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<UserDetailsDTO> login (@RequestBody UserWebsiteLoginRequestDTO dto)
            throws CaptchaServerErrorException, CaptchaInvalidException {

        UserAuthenticatedDTO authenticatedDTO = userWebsiteService.loginUser(dto);

        return new ResponseEntity<>(authenticatedDTO.getUserDetailsDTO(), authenticatedDTO.getHeaders(), HttpStatus.OK);
    }

    @PostMapping("/forgot")
    @ResponseBody
    public ResponseEntity forgotAccount (@RequestBody UserWebsiteForgotPasswordRequestDTO requestDTO)
            throws CaptchaServerErrorException, CaptchaInvalidException {
        boolean result = userWebsiteService.forgotPassword(requestDTO);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/forgot/resetPassword")
    @ResponseBody
    public ResponseEntity resetPassword (@RequestBody UserWebsitePasswordResetRequestDTO pwResetDTO) {
        boolean result = userWebsiteService.resetPassword(pwResetDTO);
        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @PostMapping("/verify")
    @ResponseBody
    public ResponseEntity verifyAccount (
            @RequestParam(value = "email")String email,
            @RequestParam(value = "verificationToken") String verificationToken
    ) {
        boolean result = userWebsiteService.verifyEmailToken(email, verificationToken);
        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
