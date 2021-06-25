package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.PostComment.*;
import com.bhtcnpm.website.model.validator.dto.Post.PostID;
import com.bhtcnpm.website.model.validator.dto.PostComment.PostCommentID;
import com.bhtcnpm.website.model.validator.dto.PostComment.PostCommentStatisticRequestSize;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

public interface PostCommentService {
    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).READ_PERMISSION)")
    PostCommentListDTO getPostCommentsByPostID (@PostID Long postID, Pageable pageable);

    @PreAuthorize(value = "hasPermission(#parentCommentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POSTCOMMENT_OBJECT, " +
                "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostCommentActionPermissionRequest).READ_PERMISSION)")
    List<PostCommentChildDTO> getChildComments (@PostCommentID Long parentCommentID, Pageable pageable);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).COMMENT_PERMISSION)")
    PostCommentDTO postPostComment (@Valid PostCommentRequestDTO postCommentRequestDTO, @PostID Long postID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#parentCommentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POSTCOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostCommentActionPermissionRequest).REPLY_PERMISSION)")
    PostCommentChildDTO postChildComment (@Valid PostCommentRequestDTO postCommentRequestDTO, @PostCommentID Long parentCommentID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POSTCOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostCommentActionPermissionRequest).UPDATE_PERMISSION)")
    PostCommentDTO putPostComment (@Valid PostCommentRequestDTO postCommentRequestDTO, @PostCommentID Long commentID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POSTCOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostCommentActionPermissionRequest).DELETE_PERMISSION)")
    boolean deletePostComment (@PostCommentID Long commentID);

    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POSTCOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostCommentActionPermissionRequest).LIKE_PERMISSION)")
    boolean createUserPostCommentLike (@PostCommentID Long commentID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POSTCOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostCommentActionPermissionRequest).LIKE_PERMISSION)")
    boolean deleteUserPostCommentLike (@PostCommentID Long commentID, Authentication authentication);

    @PreAuthorize(value = "permitAll()")
    List<PostCommentStatisticDTO> getCommentStatistics (@PostCommentStatisticRequestSize List<@PostCommentID Long> commentIDs, Authentication authentication);

    @PreAuthorize(value = "permitAll()")
    List<PostCommentAvailableActionDTO> getAvailablePostCommentAction (List<Long> postCommentIDs, Authentication authentication);
}
