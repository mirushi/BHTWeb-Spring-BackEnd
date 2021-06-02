package com.bhtcnpm.website.model.dto.UserWebsite;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDetailsDTO {
    private UUID id;
    private String name;
    private String displayName;
    private String email;
    private Long reputationScore;
    private String avatarURL;
}
