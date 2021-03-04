package com.bhtcnpm.website.model.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostDetailsWithStateListDTO {
    private List<PostDetailsWithStateDTO> postSummaryWithStateDTOs;
    private Integer totalPages;
    private Long totalElements;
}
