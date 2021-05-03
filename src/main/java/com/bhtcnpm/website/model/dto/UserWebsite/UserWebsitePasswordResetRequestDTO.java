package com.bhtcnpm.website.model.dto.UserWebsite;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class UserWebsitePasswordResetRequestDTO {
    @Email
    private String email;

    private String token;

    private String password;
}
