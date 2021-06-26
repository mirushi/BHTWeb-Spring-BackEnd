package com.bhtcnpm.website.controller.Subject;

import com.bhtcnpm.website.model.dto.Subject.SubjectFacultySummaryDTO;
import com.bhtcnpm.website.service.Subject.SubjectFacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/subjectFaculties")
@Validated
@RequiredArgsConstructor
public class SubjectFacultyController {
    private final SubjectFacultyService subjectFacultyService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<SubjectFacultySummaryDTO>> getAllSubjectFaculty () {
        List<SubjectFacultySummaryDTO> subjectFacultySummaryDTOList = subjectFacultyService.getAllSubjectFaculties();
        return new ResponseEntity<>(subjectFacultySummaryDTOList, HttpStatus.OK);
    }
}
