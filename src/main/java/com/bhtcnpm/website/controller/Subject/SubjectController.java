package com.bhtcnpm.website.controller.Subject;

import com.bhtcnpm.website.model.dto.Subject.SubjectRequestDTO;
import com.bhtcnpm.website.model.dto.Subject.SubjectSummaryDTO;
import com.bhtcnpm.website.model.dto.Subject.SubjectSummaryWithTopicAndExercisesDTO;
import com.bhtcnpm.website.model.entity.SubjectEntities.Subject;
import com.bhtcnpm.website.service.Subject.SubjectService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.keycloak.authorization.client.util.Http;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@Validated
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<SubjectSummaryDTO>> getSubjects (
            //This input is for doc displaying purpose only.
            //This is unknown why removing this cause the doc don't display QuerydslPredicate.
            //TODO: Find a way to refactor this.
            @RequestParam(required = false) Integer thisInputIsOptionalAndDontHaveEffect,
            @QuerydslPredicate(root = Subject.class) Predicate predicate
    ) {
        List<SubjectSummaryDTO> subjectSummaryDTOList = subjectService.getSubjects(predicate);
        return new ResponseEntity<>(subjectSummaryDTOList, HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    //TODO: Add authorization here.
    public ResponseEntity<SubjectSummaryDTO> postSubject (@RequestBody SubjectRequestDTO subjectRequestDTO) {
        SubjectSummaryDTO subjectSummaryDTO = subjectService.createSubject(subjectRequestDTO);
        return new ResponseEntity<>(subjectSummaryDTO, HttpStatus.OK);
    }

    @PutMapping("{id}")
    @ResponseBody
    //TODO: Add authorization here.
    public ResponseEntity<SubjectSummaryDTO> putSubject (@PathVariable Long id, @RequestBody SubjectRequestDTO requestDTO) {
        SubjectSummaryDTO subjectSummaryDTO = subjectService.updateSubject(id, requestDTO);
        return new ResponseEntity<>(subjectSummaryDTO, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @ResponseBody
    //TODO: Add authorization here.
    public ResponseEntity deleteSubject (@PathVariable Long id) {
        Boolean deleted = subjectService.deleteSubject(id);
        if (deleted) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    //This is a cheat for speed-development of front-end.
    //This unusual API will increase coupling between backend and frontend.
    //Move the merging result operation to front-end.
    //This also makes the API became circular depending.
    //TODO: Please refactor this when you have time.
    @GetMapping("/withExerciseTopicAndExerciseList/fromExercise/{id}")
    @ResponseBody
    public ResponseEntity<SubjectSummaryWithTopicAndExercisesDTO> getExerciseSubjectSummaryWithTopicAndExercise (@PathVariable("id") Long exerciseID) {
        SubjectSummaryWithTopicAndExercisesDTO subjectSummaryWithTopicAndExercisesDTO = subjectService.getSubjectWithTopicAndExercises(exerciseID);

        return new ResponseEntity<>(subjectSummaryWithTopicAndExercisesDTO, HttpStatus.OK);
    }
}
