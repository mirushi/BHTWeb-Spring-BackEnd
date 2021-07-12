package com.bhtcnpm.website.controller.Subject;

import com.bhtcnpm.website.model.dto.Subject.SubjectGroupSummaryDTO;
import com.bhtcnpm.website.service.Subject.SubjectGroupService;
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
@RequestMapping("/subjectGroups")
@Validated
@RequiredArgsConstructor
public class SubjectGroupController {

    private final SubjectGroupService subjectGroupService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<SubjectGroupSummaryDTO>> getSubjectGroup() {
        List<SubjectGroupSummaryDTO> subjectGroupSummaryDTOList = subjectGroupService.getAllSubjectGroups();
        return new ResponseEntity<>(subjectGroupSummaryDTOList, HttpStatus.OK);
    }
}
