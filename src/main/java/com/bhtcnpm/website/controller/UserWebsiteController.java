package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.UserWebsite.*;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.exception.CaptchaInvalidException;
import com.bhtcnpm.website.model.exception.CaptchaServerErrorException;
import com.bhtcnpm.website.service.UserWebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UserWebsiteController {

    private final UserWebsiteService userWebsiteService;

    @GetMapping("/checkUserExists")
    @ResponseBody
    public ResponseEntity<List<String>> checkUserWebsiteExist (
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "displayName", required = false) String displayName,
            @RequestParam(value = "email", required = false) String email
    ) {
        List<String> existedFields = userWebsiteService.checkUserExists(name, displayName, email);
        return new ResponseEntity(existedFields, HttpStatus.OK);
    }
}
