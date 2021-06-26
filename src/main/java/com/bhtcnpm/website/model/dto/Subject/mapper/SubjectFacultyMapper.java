package com.bhtcnpm.website.model.dto.Subject.mapper;

import com.bhtcnpm.website.model.dto.Subject.SubjectFacultySummaryDTO;
import com.bhtcnpm.website.model.entity.SubjectEntities.SubjectFaculty;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SubjectFacultyMapper {
    SubjectFacultySummaryDTO subjectFacultyToSubjectFacultySummaryDTO (SubjectFaculty subjectFaculty);
    List<SubjectFacultySummaryDTO> subjectFacultyListToSubjectFacultySummaryDTOList (List<SubjectFaculty> subjectFacultyList);
}
