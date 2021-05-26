package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.Post.*;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PostService {

    List<PostStatisticDTO> getPostStatistic (List<Long> postIDs, UUID userID);

    PostSummaryListDTO getPostSummary (Predicate predicate, Integer paginator, Authentication authentication);

    PostDetailsDTO getPostDetails (Long id);

    Boolean approvePost (Long postID, UUID userID);

    Boolean deletePostApproval (Long postID);

    Boolean createUserPostLike(Long postID, UUID userID);

    Boolean deleteUserPostLike(Long postID, UUID userID);

    PostDetailsDTO createPost (PostRequestDTO postRequestDTO, UUID userID);

    PostDetailsDTO editPost (PostRequestDTO postRequestDTO, Long postID, UUID userID);

    Boolean deletePost (UUID userID, Long postID);

    Boolean rejectPost (Long postID, UUID userID);

    Boolean rejectPostWithFeedback (Long postID, String feedback);

    Boolean createSavedStatus (Long postID, UUID userID);

    Boolean deleteSavedStatus (Long postID, UUID userID);

    List<PostSummaryDTO> getPostWithActivityCategory();

    List<PostSummaryDTO> getPostNewest();

    PostSummaryListDTO getPostBySearchTerm (String sortByPublishDtm, Integer page, String searchTerm, Long postCategoryID);

    PostSummaryWithStateListDTO getPostWithStateBySearchTerm (Predicate predicate, Pageable pageable);

    PostDetailsWithStateListDTO getPostDetailsWithState (Predicate predicate, Pageable pageable, PostStateType postStateType);

    PostSummaryWithStateAndFeedbackListDTO getPostWithStateAndFeedback (Predicate predicate, Pageable pageable);

    List<PostSuggestionDTO> getRelatedPostSameAuthor (UUID authorID, Long postID, Integer page) throws IDNotFoundException, IOException;

    List<PostSuggestionDTO> getRelatedPostSameCategory (Long categoryID, Long postID, Integer page) throws IDNotFoundException, IOException;

    PostSummaryListDTO getPostSavedByUserID (UUID userID, Pageable pageable);

    PostSummaryWithStateListDTO getManagementPost (String searchTerm, PostStateType postStateType, Integer page, String sortByPublishDtm, Long postCategoryID);
}
