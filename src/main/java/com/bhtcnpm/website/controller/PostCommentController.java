package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.PostComment.PostCommentChildDTO;
import com.bhtcnpm.website.model.dto.PostComment.PostCommentDTO;
import com.bhtcnpm.website.model.dto.PostComment.PostCommentListDTO;
import com.bhtcnpm.website.model.dto.PostComment.PostCommentRequestDTO;
import com.bhtcnpm.website.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @GetMapping("/posts/{postID}/comments")
    @ResponseBody
    public ResponseEntity<PostCommentListDTO> getPostComment (
            @PathVariable Long postID,
            @PageableDefault @Nullable Pageable pageable
    ) {
        PostCommentListDTO postCommentDTOs = postCommentService.getPostCommentsByPostID(postID, pageable);
        return new ResponseEntity<>(postCommentDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/posts/comments/{commentID}/children")
    @ResponseBody
    public ResponseEntity<List<PostCommentChildDTO>> getChildComments (@PathVariable Long commentID) {
        List<PostCommentChildDTO> postCommentDTOs = postCommentService.getChildComments(commentID);

        return new ResponseEntity<>(postCommentDTOs, HttpStatus.OK);
    }

    @PostMapping(value = "/posts/{postID}/comments")
    @ResponseBody
    public ResponseEntity<PostCommentDTO> postComments(@PathVariable Long postID, @RequestBody PostCommentRequestDTO postCommentRequestDTO) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        UUID userID = DemoUserIDConstant.userID;

        PostCommentDTO dto = postCommentService.postPostComment(postCommentRequestDTO, postID, userID);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping(value = "/posts/comments/{parentCommentID}")
    @ResponseBody
    public ResponseEntity<PostCommentChildDTO> postChildComment (@PathVariable Long parentCommentID, @RequestBody PostCommentRequestDTO postCommentRequestDTO) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        UUID userID = DemoUserIDConstant.userID;

        PostCommentChildDTO dto = postCommentService.postChildComment(postCommentRequestDTO, parentCommentID, userID);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(value = "/posts/comments/{commentID}")
    @ResponseBody
    public ResponseEntity<PostCommentDTO> putComment (@PathVariable Long commentID, @RequestBody PostCommentRequestDTO postCommentRequestDTO) {
        //TODO: We'll use a hard-coded userID for now. We'll get userID from user login token later.
        UUID userID = DemoUserIDConstant.userID;

        PostCommentDTO postCommentDTO = postCommentService.putPostComment(postCommentRequestDTO, commentID, userID);

        return new ResponseEntity<>(postCommentDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/posts/comments/{commentID}")
    @ResponseBody
    public ResponseEntity deleteComment (@PathVariable Long commentID) {
        boolean result = postCommentService.deletePostComment(commentID);
        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

}
