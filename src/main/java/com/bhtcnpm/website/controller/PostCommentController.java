package com.bhtcnpm.website.controller;

import com.bhtcnpm.website.model.dto.PostComment.*;
import com.bhtcnpm.website.model.validator.dto.Post.PostID;
import com.bhtcnpm.website.model.validator.dto.PostComment.PostCommentID;
import com.bhtcnpm.website.model.validator.dto.PostComment.PostCommentStatisticRequestSize;
import com.bhtcnpm.website.service.PostCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
            @PathVariable @PostID Long postID,
            @PageableDefault @Nullable Pageable pageable
    ) {
        PostCommentListDTO postCommentDTOs = postCommentService.getPostCommentsByPostID(postID, pageable);
        return new ResponseEntity<>(postCommentDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/posts/comments/{commentID}/children")
    @ResponseBody
    public ResponseEntity<List<PostCommentChildDTO>> getChildComments (@PathVariable @PostCommentID Long commentID,
                                                                       @PageableDefault @Nullable Pageable pageable) {
        List<PostCommentChildDTO> postCommentDTOs = postCommentService.getChildComments(commentID, pageable);

        return new ResponseEntity<>(postCommentDTOs, HttpStatus.OK);
    }

    @PostMapping(value = "/posts/{postID}/comments")
    @ResponseBody
    public ResponseEntity<PostCommentDTO> postComments(@PathVariable @PostID Long postID,
                                                       @RequestBody @Valid PostCommentRequestDTO postCommentRequestDTO,
                                                       Authentication authentication) {
        PostCommentDTO dto = postCommentService.postPostComment(postCommentRequestDTO, postID, authentication);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping(value = "/posts/comments/{parentCommentID}")
    @ResponseBody
    public ResponseEntity<PostCommentChildDTO> postChildComment (@PathVariable @PostCommentID Long parentCommentID,
                                                                 @RequestBody @Valid PostCommentRequestDTO postCommentRequestDTO,
                                                                 Authentication authentication) {
        PostCommentChildDTO dto = postCommentService.postChildComment(postCommentRequestDTO, parentCommentID, authentication);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(value = "/posts/comments/{commentID}")
    @ResponseBody
    public ResponseEntity<PostCommentDTO> putComment (@PathVariable @PostCommentID Long commentID,
                                                      @RequestBody @Valid PostCommentRequestDTO postCommentRequestDTO,
                                                      Authentication authentication) {
        PostCommentDTO postCommentDTO = postCommentService.putPostComment(postCommentRequestDTO, commentID, authentication);

        return new ResponseEntity<>(postCommentDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/posts/comments/{commentID}")
    @ResponseBody
    public ResponseEntity deleteComment (@PathVariable @PostCommentID Long commentID) {
        boolean result = postCommentService.deletePostComment(commentID);
        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/posts/comments/{commentID}/likeStatus")
    @ResponseBody
    public ResponseEntity postLikeStatus (@PathVariable @PostCommentID Long commentID, Authentication authentication) {
        boolean result = postCommentService.createUserPostCommentLike(commentID, authentication);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/posts/comments/{commentID}/likeStatus")
    @ResponseBody
    public ResponseEntity deleteLikeStatus (@PathVariable @PostCommentID Long commentID, Authentication authentication) {
        boolean result = postCommentService.deleteUserPostCommentLike(commentID, authentication);

        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/posts/comments/statistics")
    @ResponseBody
    public ResponseEntity<List<PostCommentStatisticDTO>> getPostCommentStatistics (
            @RequestParam @PostCommentStatisticRequestSize List<@PostCommentID Long> commentIDs,
            Authentication authentication
    ) {
        List<PostCommentStatisticDTO> postCommentStatisticDTOs = postCommentService.getCommentStatistics(commentIDs, authentication);

        return new ResponseEntity<>(postCommentStatisticDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/posts/comments/actionAvailable")
    @ResponseBody
    public ResponseEntity<List<PostCommentAvailableActionDTO>> getPostCommentActionAvailable (
            @RequestParam List<Long> postCommentIDs,
            Authentication authentication
    ) {
        List<PostCommentAvailableActionDTO> availableActionDTOList = postCommentService.getAvailablePostCommentAction(postCommentIDs, authentication);

        return new ResponseEntity<>(availableActionDTOList, HttpStatus.OK);
    }

}
