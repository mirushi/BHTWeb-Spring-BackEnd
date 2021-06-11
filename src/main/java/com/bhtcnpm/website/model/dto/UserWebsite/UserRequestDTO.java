package com.bhtcnpm.website.model.dto.UserWebsite;

import com.bhtcnpm.website.model.validator.dto.UserWebsite.UserWebsiteAboutMe;
import com.bhtcnpm.website.model.validator.dto.UserWebsite.UserWebsiteDisplayName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserRequestDTO {
    @UserWebsiteDisplayName
    private String displayName;
    @UserWebsiteAboutMe
    private String aboutMe;
}
