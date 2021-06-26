package com.bhtcnpm.website.service.Subject;

import com.bhtcnpm.website.model.dto.Subject.SubjectRequestDTO;
import com.bhtcnpm.website.model.dto.Subject.SubjectSummaryDTO;
import com.bhtcnpm.website.model.dto.Subject.SubjectSummaryWithTopicAndExercisesDTO;
import com.querydsl.core.types.Predicate;

import java.util.List;

public interface SubjectService {
    List<SubjectSummaryDTO> getSubjects (Predicate predicate);
    SubjectSummaryWithTopicAndExercisesDTO getSubjectWithTopicAndExercises(Long exerciseID);
    //TODO: Perform authorization.
    SubjectSummaryDTO createSubject (SubjectRequestDTO requestDTO);
    //TODO: Perform authorization.
    SubjectSummaryDTO updateSubject (Long subjectID, SubjectRequestDTO requestDTO);
    //TODO: Perform authorization.
    Boolean deleteSubject (Long subjectID);
}
