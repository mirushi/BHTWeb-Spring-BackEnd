package com.bhtcnpm.website.model.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostSummaryWithStateAndFeedbackListDTO {
    List<PostSummaryWithStateAndFeedbackDTO> dtos;
    private Integer totalPages;
    private Long totalElements;
}
