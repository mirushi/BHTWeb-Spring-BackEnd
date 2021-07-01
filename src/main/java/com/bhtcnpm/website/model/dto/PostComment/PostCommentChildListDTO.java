package com.bhtcnpm.website.model.dto.PostComment;

import lombok.Data;

import java.util.List;

@Data
public class PostCommentChildListDTO {
    private List<PostCommentChildDTO> postCommentChildDTOs;
    private Integer totalPages;
    private Long totalElements;
}
