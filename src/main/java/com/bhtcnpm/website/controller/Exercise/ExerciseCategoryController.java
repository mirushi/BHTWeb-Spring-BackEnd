package com.bhtcnpm.website.controller.Exercise;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exercises/categories")
@Validated
@RequiredArgsConstructor
public class ExerciseCategoryController {
}
