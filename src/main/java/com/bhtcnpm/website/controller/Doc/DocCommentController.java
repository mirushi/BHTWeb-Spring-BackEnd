package com.bhtcnpm.website.controller.Doc;

import com.bhtcnpm.website.model.dto.DocComment.*;
import com.bhtcnpm.website.model.validator.dto.Doc.DocID;
import com.bhtcnpm.website.service.Doc.DocCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
public class DocCommentController {
    private final DocCommentService docCommentService;

    @GetMapping("/documents/{docID}/comments")
    @ResponseBody
    public ResponseEntity<DocCommentListDTO> getDocComment (
            @PathVariable @DocID Long docID,
            @PageableDefault @Nullable Pageable pageable
    ) {
        DocCommentListDTO docCommentDTOs = docCommentService.getDocCommentsByDocID(docID, pageable);
        return new ResponseEntity<>(docCommentDTOs, HttpStatus.OK);
    }

    @GetMapping("/documents/comments/{commentID}/children")
    @ResponseBody
    public ResponseEntity<List<DocCommentChildDTO>> getChildComments (@PathVariable Long commentID,
                                                                      @PageableDefault @Nullable Pageable pageable) {
        List<DocCommentChildDTO> docCommentDTOs = docCommentService.getChildComments(commentID, pageable);

        return new ResponseEntity<>(docCommentDTOs, HttpStatus.OK);
    }

    @PostMapping("/documents/{docID}/comments")
    @ResponseBody
    public ResponseEntity<DocCommentDTO> postComment (@PathVariable Long docID,
                                                      @RequestBody DocCommentRequestDTO docCommentRequestDTO,
                                                      Authentication authentication) {
        DocCommentDTO dto = docCommentService.postDocComment(docCommentRequestDTO, docID, authentication);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/documents/comments/{parentCommentID}")
    @ResponseBody
    public ResponseEntity<DocCommentChildDTO> postChildComment (@PathVariable Long parentCommentID,
                                                                @RequestBody DocCommentRequestDTO docCommentRequestDTO,
                                                                Authentication authentication) {
        DocCommentChildDTO dto = docCommentService.postChildComment(docCommentRequestDTO, parentCommentID, authentication);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/documents/comments/{commentID}")
    @ResponseBody
    public ResponseEntity<DocCommentDTO> putComment (@PathVariable Long commentID,
                                                     @RequestBody DocCommentRequestDTO docCommentRequestDTO,
                                                     Authentication authentication) {
        DocCommentDTO docCommentDTO = docCommentService.putDocComment(docCommentRequestDTO, commentID, authentication);

        return new ResponseEntity<>(docCommentDTO, HttpStatus.OK);
    }

    @DeleteMapping("/documents/comments/{commentID}")
    @ResponseBody
    public ResponseEntity deleteComment (@PathVariable Long commentID) {
        boolean result = docCommentService.deleteDocComment(commentID);
        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/documents/comments/{commentID}/likeStatus")
    @ResponseBody
    public ResponseEntity postLikeStatus (@PathVariable Long commentID, Authentication authentication) {
        boolean result = docCommentService.createUserDocCommentLike(commentID, authentication);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/documents/comments/{commentID}/likeStatus")
    @ResponseBody
    public ResponseEntity deleteLikeStatus (@PathVariable Long commentID, Authentication authentication) {
        boolean result = docCommentService.deleteUserDocCommentLike(commentID, authentication);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/documents/comments/statistics")
    @ResponseBody
    public ResponseEntity<List<DocCommentStatisticDTO>> getDocCommentStatistics (
            @RequestParam List<Long> commentIDs,
            Authentication authentication
    ) {
        List<DocCommentStatisticDTO> docCommentStatisticDTOs = docCommentService.getCommentStatistics(commentIDs, authentication);

        return new ResponseEntity<>(docCommentStatisticDTOs, HttpStatus.OK);
    }

    @GetMapping("/documents/comments/actionAvailable")
    @ResponseBody
    public ResponseEntity<List<DocCommentAvailableActionDTO>> getDocCommentActionAvailable (
            @RequestParam List<Long> docCommentIDs,
            Authentication authentication
    ) {
        List<DocCommentAvailableActionDTO> availableActionDTOList = docCommentService.getAvailableDocCommentAction(docCommentIDs, authentication);

        return new ResponseEntity<>(availableActionDTOList, HttpStatus.OK);
    }
}
