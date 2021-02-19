package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.UserWebsite.UserWebsiteCreateNewRequestDTO;
import com.bhtcnpm.website.service.UserWebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UserWebsiteController {

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators();
    }

    private final UserWebsiteService userWebsiteService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<UserWebsiteCreateNewRequestDTO> register (@RequestBody UserWebsiteCreateNewRequestDTO dto) {
        return null;
    }
}
