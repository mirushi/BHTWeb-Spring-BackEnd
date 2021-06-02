package com.bhtcnpm.website.model.dto.UserWebsite;

import lombok.Data;

import java.util.UUID;

@Data
public class UserSummaryDTO {
    private UUID id;
    private String avatarURL;
    private String name;
}
