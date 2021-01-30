package com.bhtcnpm.website.model.dto.Doc;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocCommentStatisticDTO {
    private Long docID;
    private Long commentCount;
}
