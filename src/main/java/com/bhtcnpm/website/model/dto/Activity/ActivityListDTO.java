package com.bhtcnpm.website.model.dto.Activity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ActivityListDTO {
    private List<ActivityDTO> activities;
    private Integer totalPages;
    private Long totalElements;
}
