package com.bhtcnpm.website.model.dto.Exercise;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExerciseSummaryWithTopicDTO {
    private Long id;
    private String title;
    private String description;
    private Boolean attempted;
    private Long topicID;
    private String topicName;

    public ExerciseSummaryWithTopicDTO (Long id, String title, String description,Long topicID, String topicName, Boolean attempted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.topicID = topicID;
        this.topicName = topicName;
        this.attempted = attempted;
    }
}
