package com.bhtcnpm.website.controller.Post;

import com.bhtcnpm.website.model.dto.PostCategory.PostCategoryDTO;
import com.bhtcnpm.website.model.dto.PostCategory.PostCategoryRequestDTO;
import com.bhtcnpm.website.model.validator.dto.PostCategory.PostCategoryID;
import com.bhtcnpm.website.service.PostCategoryService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<PostCategoryDTO> postPostCategory (@RequestBody @Valid PostCategoryRequestDTO postCategoryDTO) {
        return new ResponseEntity<>(postCategoryService.postPostCategory(postCategoryDTO), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity<PostCategoryDTO> putPostCategory (@PathVariable @PostCategoryID Long id ,
                                                            @RequestBody @Valid PostCategoryRequestDTO postCategoryRequestDTO) {
        return new ResponseEntity<>(postCategoryService.putPostCategory(postCategoryRequestDTO, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity deletePostCategory (@PathVariable @PostCategoryID Long id) {
        postCategoryService.deletePostCategory(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
