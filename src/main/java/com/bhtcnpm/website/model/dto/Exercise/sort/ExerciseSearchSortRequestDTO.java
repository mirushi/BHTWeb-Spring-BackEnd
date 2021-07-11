package com.bhtcnpm.website.model.dto.Exercise.sort;

import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import lombok.Data;

@Data
public class ExerciseSearchSortRequestDTO {
    private ApiSortOrder sortByPublishDtm;
    private ApiSortOrder sortByAttempts;
}
