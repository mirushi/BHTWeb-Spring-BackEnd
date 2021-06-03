package com.bhtcnpm.website.model.dto.PostCommentReport;

import lombok.Data;

import java.util.List;

@Data
public class PostCommentReportRequestDTO {
    private List<Long> reasonIds;
    private String feedback;
}
