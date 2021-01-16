package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.Doc.DocDetailsDTO;
import com.bhtcnpm.website.model.dto.Doc.DocDetailsListDTO;
import com.bhtcnpm.website.model.entity.Doc;
import com.bhtcnpm.website.service.DocService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Formula;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

    @RestController
    @RequestMapping("/documents")
    @Validated
    @RequiredArgsConstructor
public class DocController {

    private final DocService docService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<DocDetailsListDTO> getAllDocuments (@QuerydslPredicate(root = Doc.class) Predicate predicate, @Min(0) Integer paginator) {

        DocDetailsListDTO result = docService.getAllDoc(predicate,paginator);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
