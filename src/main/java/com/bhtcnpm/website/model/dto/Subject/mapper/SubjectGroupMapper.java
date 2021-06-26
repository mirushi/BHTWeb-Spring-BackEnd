package com.bhtcnpm.website.model.dto.Subject.mapper;

import com.bhtcnpm.website.model.dto.Subject.SubjectGroupSummaryDTO;
import com.bhtcnpm.website.model.entity.SubjectEntities.SubjectGroup;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface SubjectGroupMapper {
    SubjectGroupSummaryDTO subjectGroupToSubjectGroupSummaryDTO (SubjectGroup subjectGroup);
    List<SubjectGroupSummaryDTO> subjectGroupListToSubjectGroupSummaryDTOList (List<SubjectGroup> subjectGroupList);
}
