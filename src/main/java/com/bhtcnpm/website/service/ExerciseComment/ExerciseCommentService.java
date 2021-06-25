package com.bhtcnpm.website.service.ExerciseComment;

import com.bhtcnpm.website.model.dto.ExerciseComment.*;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ExerciseCommentService {
    //TODO: Please change this after implement authorization for exercises.
    @PreAuthorize(value = "permitAll()")
    ExerciseCommentListDTO getExerciseCommentsByExerciseID (Long exerciseID, Pageable pageable);

    @PreAuthorize(value = "hasPermission(#parentCommentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).EXERCISECOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseCommentActionPermissionRequest).READ_PERMISSION)")
    List<ExerciseCommentChildDTO> getChildComments (Long parentCommentID, Pageable pageable);

    //TODO: Please change this after implement authorization for exercises.
    @PreAuthorize(value = "isAuthenticated()")
    ExerciseCommentDTO postExerciseComment (ExerciseCommentRequestDTO exerciseCommentRequestDTO, Long exerciseID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#parentCommentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).EXERCISECOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseCommentActionPermissionRequest).REPLY_PERMISSION)")
    ExerciseCommentChildDTO postChildComment (ExerciseCommentRequestDTO exerciseCommentRequestDTO, Long parentCommentID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).EXERCISECOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseCommentActionPermissionRequest).UPDATE_PERMISSION)")
    ExerciseCommentDTO putExerciseComment (ExerciseCommentRequestDTO exerciseCommentRequestDTO, Long commentID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).EXERCISECOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseCommentActionPermissionRequest).DELETE_PERMISSION)")
    boolean deleteExerciseComment (Long commentID);

    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).EXERCISECOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseCommentActionPermissionRequest).LIKE_PERMISSION)")
    boolean createUserExerciseCommentLike (Long commentID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).EXERCISECOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.ExerciseCommentActionPermissionRequest).LIKE_PERMISSION)")
    boolean deleteUserExerciseCommentLike (Long commentID, Authentication authentication);

    @PreAuthorize(value = "permitAll()")
    List<ExerciseCommentStatisticDTO> getCommentStatistics (List<Long> commentIDs, Authentication authentication);

    @PreAuthorize(value = "permitAll()")
    List<ExerciseCommentAvailableActionDTO> getAvailableExerciseCommentAction (List<Long> exerciseCommentIDs, Authentication authentication);
}
