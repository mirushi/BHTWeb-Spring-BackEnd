package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;

import java.util.List;

@Data
public class ExerciseSearchResultDTOList {
    private List<ExerciseSearchResultDTO> exerciseSearchResultDTOs;
    private Integer totalPages;
    private Long totalElements;
}
