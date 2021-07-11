package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.constant.sort.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSearchResultDTOList;
import com.bhtcnpm.website.model.dto.Exercise.filter.ExerciseSearchFilterRequestDTO;
import com.bhtcnpm.website.model.dto.Exercise.sort.ExerciseSearchSortRequestDTO;
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
}
