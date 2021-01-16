package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.Doc.DocDetailsDTO;
import com.bhtcnpm.website.model.dto.Doc.DocDetailsListDTO;
import com.bhtcnpm.website.model.dto.Doc.DocRequestDTO;
import com.bhtcnpm.website.model.entity.Doc;
import com.bhtcnpm.website.service.DocService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Formula;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/documents")
@Validated
@RequiredArgsConstructor
public class DocController {

    private final DocService docService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<DocDetailsListDTO> getAllDocuments (@QuerydslPredicate(root = Doc.class) Predicate predicate, @NotNull @Min(0) Integer paginator) {
        DocDetailsListDTO result = docService.getAllDoc(predicate,paginator);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity<DocDetailsDTO> putDocument (@PathVariable Long id, @RequestBody DocRequestDTO docRequestDTO) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;
        docService.putDoc(id, userID, docRequestDTO);

        return null;
    }

}
