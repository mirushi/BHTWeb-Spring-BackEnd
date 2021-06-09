package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.PostComment.*;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface PostCommentService {
    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.PostActionPermissionRequest).READ_PERMISSION)")
    PostCommentListDTO getPostCommentsByPostID (Long postID, Pageable pageable);

    @PreAuthorize(value = "permitAll()")
    List<PostCommentChildDTO> getChildComments (Long parentCommentID, Pageable pageable);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.PostActionPermissionRequest).READ_PERMISSION) and " +
            "hasRole(T(com.bhtcnpm.website.constant.security.permission.PostCommentPermissionConstant).POSTCOMMENT_PUBLIC_SELF_CREATE)")
    PostCommentDTO postPostComment (PostCommentRequestDTO postCommentRequestDTO, Long postID, Authentication authentication);

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.PostCommentPermissionConstant).POSTCOMMENT_PUBLIC_SELF_CREATE)")
    PostCommentChildDTO postChildComment (PostCommentRequestDTO postCommentRequestDTO, Long parentCommentID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POSTCOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.PostCommentActionPermissionRequest).UPDATE_PERMISSION)")
    PostCommentDTO putPostComment (PostCommentRequestDTO postCommentRequestDTO, Long commentID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POSTCOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.PostCommentActionPermissionRequest).DELETE_PERMISSION)")
    boolean deletePostComment (Long commentID);

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.PostCommentPermissionConstant).POSTCOMMENT_PUBLIC_ALL_LIKE)")
    boolean createUserPostCommentLike (Long commentID, Authentication authentication);

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.PostCommentPermissionConstant).POSTCOMMENT_PUBLIC_ALL_LIKE)")
    boolean deleteUserPostCommentLike (Long commentID, Authentication authentication);

    @PreAuthorize(value = "permitAll()")
    List<PostCommentStatisticDTO> getCommentStatistics (List<Long> commentIDs, Authentication authentication);
}
