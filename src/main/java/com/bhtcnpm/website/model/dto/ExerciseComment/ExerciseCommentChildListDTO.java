package com.bhtcnpm.website.model.dto.ExerciseComment;

import lombok.Data;

import java.util.List;

@Data
public class ExerciseCommentChildListDTO {
    private List<ExerciseCommentChildDTO> exerciseCommentChildDTOs;
    private Integer totalPages;
    private Long totalElements;
}
