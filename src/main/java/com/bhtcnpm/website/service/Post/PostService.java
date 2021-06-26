package com.bhtcnpm.website.service.Post;

import com.bhtcnpm.website.model.dto.Post.*;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.bhtcnpm.website.model.validator.dto.Pagination;
import com.bhtcnpm.website.model.validator.dto.Post.PostActionRequestSize;
import com.bhtcnpm.website.model.validator.dto.Post.PostID;
import com.bhtcnpm.website.model.validator.dto.PostCategory.PostCategoryID;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.core.Authentication;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PostService {
    @PreFilter(filterTarget = "postIDs", value = "hasPermission(filterObject, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).READ_PERMISSION)")
    List<PostStatisticDTO> getPostStatistic (List<@PostID Long> postIDs, Authentication authentication);

    PostSummaryListDTO getPostSummary (Predicate predicate, Pageable pageable, boolean mostLiked, boolean mostViewed, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#id, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).READ_PERMISSION)")
    PostDetailsDTO getPostDetails (@PostID Long id);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).APPROVE_PERMISSION)")
    Boolean approvePost (@PostID Long postID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).APPROVE_PERMISSION)")
    Boolean deletePostApproval (@PostID Long postID);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).LIKE_PERMISSION)")
    Boolean createUserPostLike(@PostID Long postID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).LIKE_PERMISSION)")
    Boolean deleteUserPostLike(@PostID Long postID, Authentication authentication);

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.PostPermissionConstant).POST_PENDING_SELF_CREATE)")
    PostDetailsDTO createPost (@Valid PostRequestDTO postRequestDTO, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).UPDATE_PERMISSION)")
    PostDetailsDTO editPost (@Valid PostRequestDTO postRequestDTO, @PostID Long postID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).DELETE_PERMISSION)")
    Boolean deletePost(@PostID Long postID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).APPROVE_PERMISSION)")
    Boolean rejectPost (@PostID Long postID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).APPROVE_PERMISSION)")
    Boolean rejectPostWithFeedback (@PostID Long postID, String feedback);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).SAVE_PERMISSION)")
    Boolean createSavedStatus (@PostID Long postID, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).SAVE_PERMISSION)")
    Boolean deleteSavedStatus (@PostID Long postID, Authentication authentication);

    List<PostSummaryDTO> getPostWithActivityCategory();

    List<PostSummaryDTO> getPostNewest();

    PostSummaryListDTO getPostBySearchTerm (String sortByPublishDtm, @Pagination Integer page, String searchTerm, @PostCategoryID Long postCategoryID, Long tagID, Authentication authentication);

    PostSummaryWithStateListDTO getPostWithStateBySearchTerm (Predicate predicate, Pageable pageable);

    @PreAuthorize(value = "hasRole(T(com.bhtcnpm.website.constant.security.permission.PostPermissionConstant).POST_UNLISTED_ALL_READ)")
    PostDetailsWithStateListDTO getPostDetailsWithState (Predicate predicate, Pageable pageable, PostStateType postStateType, Authentication authentication);

    @PreAuthorize(value = "isAuthenticated()")
    PostSummaryWithStateAndFeedbackListDTO getPostWithStateAndFeedbackUserOwn(Predicate predicate, Pageable pageable, Authentication authentication);

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).READ_PERMISSION)")
    List<PostSuggestionDTO> getRelatedPostSameAuthor (UUID authorID, @PostID Long postID, @Pagination Integer page, Authentication authentication) throws IDNotFoundException, IOException;

    @PreAuthorize(value = "hasPermission(#postID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).POST_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.PostActionPermissionRequest).READ_PERMISSION)")
    List<PostSuggestionDTO> getRelatedPostSameCategory (@PostCategoryID Long categoryID, @PostID Long postID, @Pagination Integer page, Authentication authentication) throws IDNotFoundException, IOException;

    List<PostSuggestionDTO> getRelatedPostByExercise(Long exerciseID, Integer page) throws IDNotFoundException, IOException;

    @PreAuthorize(value = "isAuthenticated()")
    PostSummaryListDTO getPostSavedByUserOwn(Authentication authentication, Pageable pageable);

    @PreAuthorize(value = "isAuthenticated()")
    PostSummaryWithStateListDTO getManagementPost (String searchTerm, PostStateType postStateType, @Pagination Integer page, String sortByPublishDtm, @PostCategoryID Long postCategoryID, Authentication authentication);

    List<PostAvailableActionDTO> getAvailablePostAction (@PostActionRequestSize List<@PostID Long> postIDs, Authentication authentication);

    List<PostSummaryDTO> getTrendingPost();

    void calculateAllPageViewAvg();
}
