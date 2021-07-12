package com.bhtcnpm.website.model.dto.ExerciseComment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ExerciseCommentListDTO {
    private List<ExerciseCommentDTO> exerciseCommentDTOs;
    private Integer totalPages;
    private Long totalElements;
}
