package com.bhtcnpm.website.model.dto.UserWebsite;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDetailsDTO {
    private UUID id;
    private String name;
    private String displayName;
    private Long reputationScore;
    private String avatarURL;
    private String email;
    private String aboutMe;
}
