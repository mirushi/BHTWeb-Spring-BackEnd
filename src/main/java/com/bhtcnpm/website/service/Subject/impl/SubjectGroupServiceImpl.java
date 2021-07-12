package com.bhtcnpm.website.service.Subject.impl;

import com.bhtcnpm.website.model.dto.Subject.SubjectGroupSummaryDTO;
import com.bhtcnpm.website.model.dto.Subject.mapper.SubjectGroupMapper;
import com.bhtcnpm.website.model.entity.SubjectEntities.SubjectGroup;
import com.bhtcnpm.website.repository.Subject.SubjectGroupRepository;
import com.bhtcnpm.website.service.Subject.SubjectGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectGroupServiceImpl implements SubjectGroupService {

    private final SubjectGroupRepository subjectGroupRepository;
    private final SubjectGroupMapper subjectGroupMapper;

    @Override
    public List<SubjectGroupSummaryDTO> getAllSubjectGroups() {
        List<SubjectGroup> subjectGroupSummaryDTOList = subjectGroupRepository.findAll();
        return subjectGroupMapper.subjectGroupListToSubjectGroupSummaryDTOList(subjectGroupSummaryDTOList);
    }
}
