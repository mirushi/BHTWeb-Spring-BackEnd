package com.bhtcnpm.website.model.dto.UserWebsite;

import com.bhtcnpm.website.constant.business.UserWebsite.UWBusinessConstraint;
import com.bhtcnpm.website.model.validator.dto.UserWebsite.ValidUWCreateNewRequest;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
