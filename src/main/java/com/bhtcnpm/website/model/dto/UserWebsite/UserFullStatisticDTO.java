package com.bhtcnpm.website.model.dto.UserWebsite;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class UserFullStatisticDTO {
    private Long reputationScore;
    private Long postCount;
    private Long docCount;

    public UserFullStatisticDTO (Long reputationScore, Long postCount, Long docCount) {
        this.reputationScore = reputationScore;
        this.postCount = postCount;
        this.docCount = docCount;
    }
}
