package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.PostComment.PostCommentDTO;
import com.bhtcnpm.website.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
