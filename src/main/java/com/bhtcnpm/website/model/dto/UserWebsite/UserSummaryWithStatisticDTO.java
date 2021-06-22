package com.bhtcnpm.website.model.dto.UserWebsite;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserSummaryWithStatisticDTO {
    private UUID id;
    private String name;
    private String displayName;
    private Long reputationScore;
    private String avatarURL;
    private Long postCount;
    private Long docCount;
}
