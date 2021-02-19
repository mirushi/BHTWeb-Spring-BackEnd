package com.bhtcnpm.website.model.dto.UserWebsite;

import com.bhtcnpm.website.constant.business.UserWebsite.UWBusinessConstraint;
import com.bhtcnpm.website.model.validator.UserWebsite.ValidUWCreateNewRequest;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ValidUWCreateNewRequest
public class UserWebsiteCreateNewRequestDTO {
    @NotBlank
    @Size(min = UWBusinessConstraint.MIN_USER_NAME_LENGTH, max = UWBusinessConstraint.MAX_USER_NAME_LENGTH)
    private String name;

    @NotBlank
    @Size(min = UWBusinessConstraint.MIN_DISPLAY_NAME_LENGTH, max = UWBusinessConstraint.MAX_DISPLAY_NAME_LENGTH)
    private String displayName;

    @NotBlank
    @Email
    @Size(max = UWBusinessConstraint.MAX_EMAIL_LENGTH)
    private String email;

    @NotBlank
    @Size(min = UWBusinessConstraint.MIN_PASSWORD_LENGTH, max = UWBusinessConstraint.MAX_PASSWORD_LENGTH)
    private String password;

    @Size(max = UWBusinessConstraint.MAX_AVATAR_URL_LENGTH)
    private String avatarURL;
}
