package com.bhtcnpm.website.controller.Exercise;

import com.bhtcnpm.website.model.dto.ExerciseComment.*;
import com.bhtcnpm.website.service.ExerciseComment.ExerciseCommentService;
import lombok.RequiredArgsConstructor;
import org.keycloak.authorization.client.util.Http;
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
public class ExerciseCommentController {

    private final ExerciseCommentService exerciseCommentService;

    @GetMapping("/exercises/{exerciseID}/comments")
    @ResponseBody
    public ResponseEntity<ExerciseCommentListDTO> getExerciseComment (
            @PathVariable Long exerciseID,
            @PageableDefault @Nullable Pageable pageable
    ) {
        ExerciseCommentListDTO exerciseCommentListDTO = exerciseCommentService.getExerciseCommentsByExerciseID(exerciseID, pageable);
        return new ResponseEntity<>(exerciseCommentListDTO, HttpStatus.OK);
    }

    @GetMapping("/exercises/comments/{commentID}/children")
    @ResponseBody
    public ResponseEntity<List<ExerciseCommentChildDTO>> getChildComments (@PathVariable Long commentID,
                                                                           @PageableDefault @Nullable Pageable pageable){
        List<ExerciseCommentChildDTO> exerciseCommentDTOs = exerciseCommentService.getChildComments(commentID, pageable);

        return new ResponseEntity<>(exerciseCommentDTOs, HttpStatus.OK);
    }

    @PostMapping(value = "/exercises/{exerciseID}/comments")
    @ResponseBody
    public ResponseEntity<ExerciseCommentDTO> postComment (@PathVariable Long exerciseID,
                                                           @RequestBody ExerciseCommentRequestDTO exerciseCommentRequestDTO,
                                                           Authentication authentication) {
        ExerciseCommentDTO dto = exerciseCommentService.postExerciseComment(exerciseCommentRequestDTO, exerciseID, authentication);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping(value = "/exercises/comments/{parentCommentID}")
    @ResponseBody
    public ResponseEntity<ExerciseCommentChildDTO> postChildComment (@PathVariable Long parentCommentID,
                                                                     @RequestBody ExerciseCommentRequestDTO exerciseCommentRequestDTO,
                                                                     Authentication authentication) {
        ExerciseCommentChildDTO dto = exerciseCommentService.postChildComment(exerciseCommentRequestDTO, parentCommentID, authentication);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(value = "/exercises/comments/{commentID}")
    @ResponseBody
    public ResponseEntity<ExerciseCommentDTO> putComment (@PathVariable Long commentID,
                                                          @RequestBody ExerciseCommentRequestDTO exerciseCommentRequestDTO,
                                                          Authentication authentication) {
        ExerciseCommentDTO exerciseCommentDTO = exerciseCommentService.putExerciseComment(exerciseCommentRequestDTO, commentID, authentication);
        return new ResponseEntity<>(exerciseCommentDTO, HttpStatus.OK);
    }

    @DeleteMapping(value = "/exercises/comments/{commentID}")
    @ResponseBody
    public ResponseEntity deleteComment (@PathVariable Long commentID) {
        boolean result = exerciseCommentService.deleteExerciseComment(commentID);
        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/exercises/comments/{commentID}/likeStatus")
    @ResponseBody
    public ResponseEntity postLikeStatus (@PathVariable Long commentID, Authentication authentication) {
        boolean result = exerciseCommentService.createUserExerciseCommentLike(commentID, authentication);
        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/exercises/comments/{commentID}/likeStatus")
    @ResponseBody
    public ResponseEntity deleteLikeStatus (@PathVariable Long commentID, Authentication authentication) {
        boolean result = exerciseCommentService.deleteUserExerciseCommentLike(commentID, authentication);
        if (result) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/exercises/comments/statistics")
    @ResponseBody
    public ResponseEntity<List<ExerciseCommentStatisticDTO>> getExerciseCommentStatistics (
            @RequestParam List<Long> commentIDs,
            Authentication authentication
    ) {
        List<ExerciseCommentStatisticDTO> exerciseCommentStatisticDTOs = exerciseCommentService.getCommentStatistics(commentIDs, authentication);

        return new ResponseEntity<>(exerciseCommentStatisticDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/exercises/comments/actionAvailable")
    @ResponseBody
    public ResponseEntity<List<ExerciseCommentAvailableActionDTO>> getExerciseCommentActionAvailable (
            @RequestParam List<Long> exerciseCommentIDs,
            Authentication authentication
    ) {
        List<ExerciseCommentAvailableActionDTO> availableActionDTOList = exerciseCommentService.getAvailableExerciseCommentAction(exerciseCommentIDs, authentication);
        return new ResponseEntity<>(availableActionDTOList, HttpStatus.OK);
    }

}
