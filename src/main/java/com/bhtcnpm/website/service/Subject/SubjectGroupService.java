package com.bhtcnpm.website.service.Subject;

import com.bhtcnpm.website.model.dto.Subject.SubjectGroupSummaryDTO;

import java.util.List;

public interface SubjectGroupService {
    List<SubjectGroupSummaryDTO> getAllSubjectGroups ();
}
