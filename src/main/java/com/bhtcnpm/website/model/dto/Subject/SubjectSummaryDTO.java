package com.bhtcnpm.website.model.dto.Subject;

import lombok.Data;
import lombok.Value;

@Value
public class SubjectSummaryDTO {
    Long id;
    String name;
    String description;
    String imageURL;
}
