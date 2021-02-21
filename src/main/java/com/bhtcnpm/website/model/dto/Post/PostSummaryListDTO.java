package com.bhtcnpm.website.model.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostSummaryListDTO {
    private List<PostSummaryDTO> postSummaryDTOs;
    private Integer totalPages;
    private Long totalElements;
}
