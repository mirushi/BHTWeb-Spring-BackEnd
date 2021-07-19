package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTOList;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSuggestionDTO;
import com.bhtcnpm.website.model.dto.Exercise.filter.ExerciseSearchFilterRequestDTO;
import com.bhtcnpm.website.model.dto.Exercise.sort.ExerciseSearchSortRequestDTO;
import com.bhtcnpm.website.service.Exercise.ExerciseSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
@Validated
@RequiredArgsConstructor
public class ExerciseSearchController {

    private final ExerciseSearchService exerciseSearchService;

    @GetMapping("/exercises/searchFilter")
    @ResponseBody
    public ResponseEntity<ExerciseSearchResultDTOList> searchExercise (
            ExerciseSearchFilterRequestDTO filterRequestDTO,
            @RequestParam(value = "page") Integer page,
            ExerciseSearchSortRequestDTO sortRequestDTO,
            Authentication authentication) {
        ExerciseSearchResultDTOList dtoList = exerciseSearchService.searchExercise(
                filterRequestDTO,
                sortRequestDTO,
                page,
                authentication
        );

        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @GetMapping("/exercises/related")
    @ResponseBody
    public ResponseEntity<List<ExerciseSuggestionDTO>> related (@RequestParam(value = "postID", required = false) Long postID,
                                                                @RequestParam(value = "docID", required = false) Long docID,
                                                                @RequestParam(value = "page", required = false) Integer page,
                                                                Authentication authentication) throws IOException {
        return new ResponseEntity<>(exerciseSearchService.getRelatedExercises(postID, docID, page, authentication), HttpStatus.OK);
    }
}
