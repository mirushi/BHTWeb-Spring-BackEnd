package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.DocComment.DocCommentListDTO;
import com.bhtcnpm.website.model.dto.DocComment.DocCommentRequestDTO;
import com.bhtcnpm.website.service.DocCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/documents/{id}/comments")
@Validated
@RequiredArgsConstructor
public class DocCommentController {

    private final DocCommentService docCommentService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<DocCommentListDTO> getDocComment (@RequestParam Integer paginator, @PathVariable Long id) {
        return new ResponseEntity<>(docCommentService.getDocComment(paginator, id), HttpStatus.OK);
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity postDocComment (@RequestBody DocCommentRequestDTO requestDTO, @PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        Long userID = 1L;

        HttpStatus status;
        Boolean result = docCommentService.postDocComment(requestDTO, userID, id);
        if (result) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity(status);
    }

}
