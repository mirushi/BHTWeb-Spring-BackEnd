package com.bhtcnpm.website.model.dto.Activity;

import com.bhtcnpm.website.model.dto.Activity.ActivityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ActivityListDTO {

    private List<ActivityDTO> activities;

    private Integer totalPages;

}
