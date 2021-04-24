package com.bhtcnpm.website.model.dto.UserWebsite;

import lombok.Data;

@Data
//TODO: Username and email cannot be co-exist. There must be only one field and other is null.
public class UserWebsiteForgotPasswordRequestDTO {
    private String username;
    private String email;
}
