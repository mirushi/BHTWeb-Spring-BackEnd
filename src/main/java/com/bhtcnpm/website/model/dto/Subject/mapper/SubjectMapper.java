package com.bhtcnpm.website.model.dto.Subject.mapper;

import com.bhtcnpm.website.model.dto.Subject.SubjectRequestDTO;
import com.bhtcnpm.website.model.dto.Subject.SubjectSummaryDTO;
import com.bhtcnpm.website.model.entity.SubjectEntities.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper
public interface SubjectMapper {
    SubjectSummaryDTO subjectToSubjectSummaryDTO (Subject subject);
    List<SubjectSummaryDTO> subjectIterableToSubjectSummaryDTOList (Iterable<Subject> subjectIterable);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "docs", ignore = true)
    @Mapping(target = "version", ignore = true)
    Subject subjectDTORequestToSubject (SubjectRequestDTO requestDTO, @MappingTarget Subject subject);
}
