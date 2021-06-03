package com.bhtcnpm.website.model.dto.PostCommentReport;

import com.bhtcnpm.website.model.entity.PostCommentEntities.PostCommentReport;
import lombok.Data;

import java.util.List;

@Data
public class PostCommentReportListDTO {
    private List<PostCommentReportDTO> postCommentReportDTOs;
    private Long totalElements;
    private Integer totalPages;
}
