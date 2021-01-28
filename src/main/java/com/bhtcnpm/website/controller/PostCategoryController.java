package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.PostCategory.PostCategoryDTO;
import com.bhtcnpm.website.service.PostCategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @ResponseBody
    public ResponseEntity<PostCategoryDTO> postPostCategory (@RequestBody PostCategoryDTO postCategoryDTO) {
        return new ResponseEntity<>(postCategoryService.postPostCategory(postCategoryDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<PostCategoryDTO> putPostCategory (@PathVariable Long id ,@RequestBody PostCategoryDTO postCategoryDTO) {
        return new ResponseEntity<>(postCategoryService.putPostCategory(postCategoryDTO, id), HttpStatus.OK);
    }
}
