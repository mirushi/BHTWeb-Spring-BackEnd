package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTOList;
import com.bhtcnpm.website.service.Exercise.ExerciseSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/exercises/searchFilter")
@Validated
@RequiredArgsConstructor
public class ExerciseSearchController {

    private final ExerciseSearchService exerciseSearchService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<ExerciseSearchResultDTOList> searchExercise (
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "categoryID", required = false) Long categoryID,
            @RequestParam(value = "subjectID", required = false) Long subjectID,
            @RequestParam(value = "authorID", required = false)UUID authorID,
            @RequestParam(value = "tags", required = false) Long tagID,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "sortByPublishDtm", required = false)ApiSortOrder sortByPublishDtm,
            Authentication authentication) {
//        ExerciseSearchResultDTOList dtoList = exerciseSearchService.searchExercise(
//                searchTerm,
//                categoryID,
//                subjectID,
//                authorID,
//                tagID,
//                page,
//                sortByPublishDtm
//        );
//
//        return new ResponseEntity<>(dtoList, HttpStatus.OK);
        return null;
    }

}
