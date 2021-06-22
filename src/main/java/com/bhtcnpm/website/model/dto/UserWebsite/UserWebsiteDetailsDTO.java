package com.bhtcnpm.website.model.dto.UserWebsite;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserWebsiteDetailsDTO {
    private String displayName;
    private String email;
    private Long reputationScore;
    private Long postCount;
    private Long docCount;
}
