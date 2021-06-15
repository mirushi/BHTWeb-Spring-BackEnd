package com.bhtcnpm.website.service.Exercise;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseTopicDTO;

import java.util.List;

public interface ExerciseTopicService {
    List<ExerciseTopicDTO> getAllExerciseTopicsBySubject (Long subjectID);

}
