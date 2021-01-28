package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.DocCategory.DocCategoryDTO;
import com.bhtcnpm.website.service.DocCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @ResponseBody
    public ResponseEntity<DocCategoryDTO> postDocCategory (@RequestBody DocCategoryDTO docCategoryDTO) {
        DocCategoryDTO docCategoryDTOnew = docCategoryService.createDocCategory(docCategoryDTO);
        return new ResponseEntity<>(docCategoryDTOnew, HttpStatus.OK);
    }

    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity<DocCategoryDTO> putDocCategory (@PathVariable Long id, @RequestBody DocCategoryDTO docCategoryDTO) {
        DocCategoryDTO docCategoryDTOnew = docCategoryService.updateDocCategory(id, docCategoryDTO);
        return new ResponseEntity<>(docCategoryDTOnew, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @ResponseBody
    public ResponseEntity deleteDocCategory (@PathVariable Long id) {
        Boolean result = docCategoryService.deleteDocCategory(id);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}
