package com.bhtcnpm.website.model.dto.PostCommentReport;

import com.bhtcnpm.website.model.entity.enumeration.PostCommentReportAction.PostCommentReportActionType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCommentReportResolveRequestDTO {
    private PostCommentReportActionType postCommentReportActionType;
    private String resolvedNote;
}
