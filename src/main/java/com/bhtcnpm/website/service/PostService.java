package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.Post.*;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.model.validator.dto.Post.PostID;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PostService {
    @PreFilter(filterTarget = "postIDs", value = "hasPermission(filterObject, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.PostInternalPermissionRequest).READ_PERMISSION)")
    List<PostStatisticDTO> getPostStatistic (List<@PostID Long> postIDs, Authentication authentication);

    PostSummaryListDTO getPostSummary (Predicate predicate, Integer paginator, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#id, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.PostInternalPermissionRequest).READ_PERMISSION)")
    PostDetailsDTO getPostDetails (@PostID Long id);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.PostInternalPermissionRequest).APPROVE_PERMISSION)")
    Boolean approvePost (@PostID Long postID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.PostInternalPermissionRequest).APPROVE_PERMISSION)")
    Boolean deletePostApproval (Long postID);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.PostInternalPermissionRequest).LIKE_PERMISSION)")
    Boolean createUserPostLike(Long postID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.PostInternalPermissionRequest).LIKE_PERMISSION)")
    Boolean deleteUserPostLike(Long postID, Authentication authentication);

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.PostPermissionConstant).POST_PENDING_SELF_CREATE)")
    PostDetailsDTO createPost (PostRequestDTO postRequestDTO, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.PostInternalPermissionRequest).UPDATE_PERMISSION)")
    PostDetailsDTO editPost (PostRequestDTO postRequestDTO, Long postID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.PostInternalPermissionRequest).DELETE_PERMISSION)")
    Boolean deletePost (Long postID);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.PostInternalPermissionRequest).APPROVE_PERMISSION)")
    Boolean rejectPost (Long postID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.PostInternalPermissionRequest).APPROVE_PERMISSION)")
    Boolean rejectPostWithFeedback (Long postID, String feedback);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.PostInternalPermissionRequest).SAVE_PERMISSION)")
    Boolean createSavedStatus (Long postID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.PostInternalPermissionRequest).SAVE_PERMISSION)")
    Boolean deleteSavedStatus (Long postID, Authentication authentication);

    List<PostSummaryDTO> getPostWithActivityCategory();

    List<PostSummaryDTO> getPostNewest();

    PostSummaryListDTO getPostBySearchTerm (String sortByPublishDtm, Integer page, String searchTerm, Long postCategoryID, Authentication authentication);

    PostSummaryWithStateListDTO getPostWithStateBySearchTerm (Predicate predicate, Pageable pageable);

    PostDetailsWithStateListDTO getPostDetailsWithState (Predicate predicate, Pageable pageable, PostStateType postStateType);

    PostSummaryWithStateAndFeedbackListDTO getPostWithStateAndFeedback (Predicate predicate, Pageable pageable);

    List<PostSuggestionDTO> getRelatedPostSameAuthor (UUID authorID, Long postID, Integer page) throws IDNotFoundException, IOException;

    List<PostSuggestionDTO> getRelatedPostSameCategory (Long categoryID, Long postID, Integer page) throws IDNotFoundException, IOException;

    PostSummaryListDTO getPostSavedByUserID (UUID userID, Pageable pageable);

    PostSummaryWithStateListDTO getManagementPost (String searchTerm, PostStateType postStateType, Integer page, String sortByPublishDtm, Long postCategoryID);
}
