package com.bhtcnpm.website.model.dto.UserWebsite;

import com.bhtcnpm.website.model.validator.dto.UserWebsite.ValidUWCreateNewRequest;
import lombok.Data;

@Data
@ValidUWCreateNewRequest
public class UserWebsiteCreateNewRequestDTO {
    private String name;

    private String displayName;

    private String email;

    private String password;

    private String avatarURL;

    private String captcha;
}
