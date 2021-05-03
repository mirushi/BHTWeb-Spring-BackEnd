package com.bhtcnpm.website.model.dto.UserWebsite;

import lombok.Data;

@Data
public class UserDetailsDTO {
    private Long id;
    private String name;
    private String displayName;
    private String email;
    private Long reputationScore;
    private String avatarURL;
}
