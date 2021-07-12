package com.bhtcnpm.website.model.dto.UserWebsite;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class UserStatisticDTO {
    private Long postCount;
    private Long docCount;

    public UserStatisticDTO(Long postCount, Long docCount) {
        this.postCount = postCount;
        this.docCount = docCount;
    }

}
