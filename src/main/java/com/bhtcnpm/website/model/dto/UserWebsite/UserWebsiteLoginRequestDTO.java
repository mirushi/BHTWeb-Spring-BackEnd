package com.bhtcnpm.website.model.dto.UserWebsite;

import com.bhtcnpm.website.model.validator.dto.UserWebsite.ValidUWLoginRequest;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@ValidUWLoginRequest
public class UserWebsiteLoginRequestDTO {
    private String username;

    @Email
    private String email;

    @NotNull
    private String password;

    private String captcha;
}
