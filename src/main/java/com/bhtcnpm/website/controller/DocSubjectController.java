package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.DocSubject.DocSubjectDTO;
import com.bhtcnpm.website.service.Doc.DocSubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documents/subjects")
@Validated
@RequiredArgsConstructor
public class DocSubjectController {
    private final DocSubjectService docSubjectService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<DocSubjectDTO>> getDocSubjects () {
        List<DocSubjectDTO> docSubjectDTOs = docSubjectService.getDocSubjects();

        return new ResponseEntity<>(docSubjectDTOs, HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<DocSubjectDTO> postDocSubject(@RequestBody DocSubjectDTO docSubjectDTO) {
        DocSubjectDTO docSubjectDTOnew = docSubjectService.createDocSubject(docSubjectDTO);
        return new ResponseEntity<>(docSubjectDTOnew, HttpStatus.OK);
    }

    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity<DocSubjectDTO> putDocSubject (@PathVariable Long id, @RequestBody DocSubjectDTO docSubjectDTO) {
        DocSubjectDTO docSubjectDTOnew = docSubjectService.updateDocSubject(id, docSubjectDTO);
        return new ResponseEntity<>(docSubjectDTOnew, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @ResponseBody
    public ResponseEntity deleteDocSubject (@PathVariable Long id) {
        Boolean result = docSubjectService.deleteDocSubject(id);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
