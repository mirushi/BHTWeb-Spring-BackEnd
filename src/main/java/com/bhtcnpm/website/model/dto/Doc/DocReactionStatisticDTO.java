package com.bhtcnpm.website.model.dto.Doc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DocReactionStatisticDTO {
    private Long docID;
    private Long likeCount;
    private Long dislikeCount;
}
