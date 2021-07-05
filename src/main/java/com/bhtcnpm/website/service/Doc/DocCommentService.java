package com.bhtcnpm.website.service.Doc;

import com.bhtcnpm.website.model.dto.DocComment.*;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface DocCommentService {
    @PreAuthorize(value = "hasPermission(#docID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOC_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest).READ_PERMISSION)")
    DocCommentListDTO getDocCommentsByDocID(Long docID, Pageable pageable);

    @PreAuthorize(value = "hasPermission(#parentCommentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOCCOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocCommentActionPermissionRequest).READ_PERMISSION)")
    List<DocCommentChildDTO> getChildComments (Long parentCommentID, Pageable pageable);

    @PreAuthorize(value = "hasPermission(#docID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOC_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest).COMMENT_PERMISSION)")
    DocCommentDTO postDocComment (DocCommentRequestDTO docCommentRequestDTO, Long docID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#parentCommentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOCCOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocCommentActionPermissionRequest).REPLY_PERMISSION)")
    DocCommentChildDTO postChildComment (DocCommentRequestDTO docCommentRequestDTO, Long parentCommentID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOCCOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocCommentActionPermissionRequest).UPDATE_PERMISSION)")
    DocCommentDTO putDocComment (DocCommentRequestDTO docCommentRequestDTO, Long commentID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOCCOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocCommentActionPermissionRequest).DELETE_PERMISSION)")
    boolean deleteDocComment (Long commentID);

    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOCCOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocCommentActionPermissionRequest).LIKE_PERMISSION)")
    boolean createUserDocCommentLike (Long commentID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#commentID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOCCOMMENT_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocCommentActionPermissionRequest).LIKE_PERMISSION)")
    boolean deleteUserDocCommentLike (Long commentID, Authentication authentication);

    @PreAuthorize(value = "permitAll()")
    List<DocCommentStatisticDTO> getCommentStatistics (List<Long> commentIDs, Authentication authentication);

    @PreAuthorize(value = "permitAll()")
    List<DocCommentAvailableActionDTO> getAvailableDocCommentAction (List<Long> docCommentIDs, Authentication authentication);
}
