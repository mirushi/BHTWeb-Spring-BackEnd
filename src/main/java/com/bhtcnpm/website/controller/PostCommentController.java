package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.PostComment.PostCommentDTO;
import com.bhtcnpm.website.model.dto.PostComment.PostCommentRequestDTO;
import com.bhtcnpm.website.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Validated
@RequiredArgsConstructor
public class PostCommentController {

    private final PostCommentService postCommentService;

    @GetMapping("/posts/{postID}/comments")
    @ResponseBody
    public ResponseEntity<List<PostCommentDTO>> getPostComment (@PathVariable Long postID) {
        List<PostCommentDTO> postCommentDTOs = postCommentService.getPostCommentsByPostID(postID);

        return new ResponseEntity<>(postCommentDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/posts/comments/{commentID}")
    @ResponseBody
    public ResponseEntity<List<PostCommentDTO>> getChildComments (@PathVariable Long commentID) {
        List<PostCommentDTO> postCommentDTOs = postCommentService.getChildComments(commentID);

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
