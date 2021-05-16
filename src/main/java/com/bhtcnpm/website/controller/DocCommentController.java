package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.DocComment.DocCommentDTO;
import com.bhtcnpm.website.model.dto.DocComment.DocCommentListDTO;
import com.bhtcnpm.website.model.dto.DocComment.DocCommentRequestDTO;
import com.bhtcnpm.website.service.DocCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/documents")
@Validated
@RequiredArgsConstructor
public class DocCommentController {

    private final DocCommentService docCommentService;

    @GetMapping("{id}/comments")
    @ResponseBody
    public ResponseEntity<DocCommentListDTO> getDocComment (@RequestParam Integer paginator, @PathVariable Long id) {
        return new ResponseEntity<>(docCommentService.getDocComment(paginator, id), HttpStatus.OK);
    }

    @PostMapping("{id}/comments")
    @ResponseBody
    public ResponseEntity postDocComment (@RequestBody DocCommentRequestDTO requestDTO, @PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        UUID userID = DemoUserIDConstant.userID;

        HttpStatus status;
        Boolean result = docCommentService.postDocComment(requestDTO, userID, id);
        if (result) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity(status);
    }

    @PutMapping("/comments/{id}")
    @ResponseBody
    public ResponseEntity<DocCommentDTO> putDocComment (@RequestBody DocCommentRequestDTO docCommentRequestDTO, @PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        UUID userID = DemoUserIDConstant.userID;

        DocCommentDTO docCommentDTO = docCommentService.putDocComment(docCommentRequestDTO, id, userID);

        return new ResponseEntity<>(docCommentDTO, HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    @ResponseBody
    public ResponseEntity deleteDocComment (@PathVariable Long id) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        UUID userID = DemoUserIDConstant.userID;

        if (docCommentService.deleteDocComment(id, userID)) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
