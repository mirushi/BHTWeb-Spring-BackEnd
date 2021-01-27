package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.PostCategory.PostCategoryDTO;
import com.bhtcnpm.website.service.PostCategoryService;
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
@RequestMapping("/posts/categories")
@Validated
@RequiredArgsConstructor
public class PostCategoryController {

    private final PostCategoryService postCategoryService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<PostCategoryDTO>> getPostCategories () {
        return new ResponseEntity<>(postCategoryService.getAllPostCategories(), HttpStatus.OK);
    }

}
