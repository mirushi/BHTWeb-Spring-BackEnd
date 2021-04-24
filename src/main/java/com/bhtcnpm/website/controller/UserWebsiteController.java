package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.UserWebsite.*;
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
    public ResponseEntity<UserDetailsDTO> register (@RequestBody UserWebsiteCreateNewRequestDTO dto) {
        UserAuthenticatedDTO authenticatedDTO = userWebsiteService.createNewNormalUser(dto);

        return new ResponseEntity<>(authenticatedDTO.getUserDetailsDTO(), authenticatedDTO.getHeaders(), HttpStatus.OK);
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<UserDetailsDTO> login (@RequestBody UserWebsiteLoginRequestDTO dto) {

        UserAuthenticatedDTO authenticatedDTO = userWebsiteService.loginUser(dto);

        return new ResponseEntity<>(authenticatedDTO.getUserDetailsDTO(), authenticatedDTO.getHeaders(), HttpStatus.OK);
    }

    @PostMapping("/forgot")
    @ResponseBody
    public ResponseEntity forgotAccount (@RequestBody UserWebsiteForgotPasswordRequestDTO requestDTO) {
        boolean result = userWebsiteService.forgotPassword(requestDTO);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/verify")
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
