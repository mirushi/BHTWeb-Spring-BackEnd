package com.bhtcnpm.website.model.dto.PostComment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PostCommentListDTO {
    private List<PostCommentDTO> postCommentDTOs;
    private Integer totalPages;
    private Long totalElements;
}
