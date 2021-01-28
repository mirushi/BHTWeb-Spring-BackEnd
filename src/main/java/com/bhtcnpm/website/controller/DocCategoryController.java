package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.DocCategory.DocCategoryDTO;
import com.bhtcnpm.website.service.DocCategoryService;
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
@RequestMapping("/documents/categories")
@Validated
@RequiredArgsConstructor
public class DocCategoryController {

    private final DocCategoryService docCategoryService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<DocCategoryDTO>> getDocCategories () {
        List<DocCategoryDTO> docCategoryDTOs = docCategoryService.getDocCategories();

        return new ResponseEntity<>(docCategoryDTOs, HttpStatus.OK);
    }

}
