package com.bhtcnpm.website.service.Subject.impl;

import com.bhtcnpm.website.model.dto.Subject.SubjectFacultySummaryDTO;
import com.bhtcnpm.website.model.dto.Subject.mapper.SubjectFacultyMapper;
import com.bhtcnpm.website.model.entity.SubjectEntities.SubjectFaculty;
import com.bhtcnpm.website.repository.Subject.SubjectFacultyRepository;
import com.bhtcnpm.website.service.Subject.SubjectFacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectFacultyServiceImpl implements SubjectFacultyService {

    private final SubjectFacultyRepository subjectFacultyRepository;
    private final SubjectFacultyMapper subjectFacultyMapper;

    @Override
    public List<SubjectFacultySummaryDTO> getAllSubjectFaculties() {
        List<SubjectFaculty> subjectFacultyList = subjectFacultyRepository.findAll();
        return subjectFacultyMapper.subjectFacultyListToSubjectFacultySummaryDTOList(subjectFacultyList);
    }
}
