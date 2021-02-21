package com.bhtcnpm.website.model.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostSummaryWithStateListDTO {
    private List<PostSummaryWithStateDTO> postSummaryWithStateDTOs;
    private Integer totalPages;
    private Long totalElements;
}
