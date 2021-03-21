package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.Post.*;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.model.exception.IDNotFoundException;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface PostService {

    List<PostStatisticDTO> getPostStatistic (List<Long> postIDs, Long userID);

    PostSummaryListDTO getPostSummary (Predicate predicate, Integer paginator);

    PostDetailsDTO getPostDetails (Long id);

    Boolean approvePost (Long postID, Long userID);

    Boolean deletePostApproval (Long postID);

    Boolean createUserPostLike(Long postID, Long userID);

    Boolean deleteUserPostLike(Long postID, Long userID);

    PostDetailsDTO createPost (PostRequestDTO postRequestDTO, Long userID);

    PostDetailsDTO editPost (PostRequestDTO postRequestDTO, Long postID, Long userID);

    Boolean deletePost (Long userID, Long postID);

    Boolean rejectPost (Long postID, Long userID);

    Boolean rejectPostWithFeedback (Long postID, String feedback);

    Boolean createSavedStatus (Long postID, Long userID);

    Boolean deleteSavedStatus (Long postID, Long userID);

    List<PostSummaryDTO> getPostWithActivityCategory();

    List<PostSummaryDTO> getPostNewest();

    PostSummaryListDTO getPostBySearchTerm (String sortByPublishDtm, Integer page, String searchTerm, Long postCategoryID);

    PostSummaryWithStateListDTO getPostWithStateBySearchTerm (Predicate predicate, Pageable pageable);

    PostDetailsWithStateListDTO getPostDetailsWithState (Predicate predicate, Pageable pageable, PostStateType postStateType);

    PostSummaryWithStateAndFeedbackListDTO getPostWithStateAndFeedback (Predicate predicate, Pageable pageable);

    List<PostSuggestionDTO> getRelatedPostSameAuthor (Long authorID, Long postID, Integer page) throws IDNotFoundException, IOException;

    List<PostSuggestionDTO> getRelatedPostSameCategory (Long categoryID, Long postID, Integer page) throws IDNotFoundException, IOException;
}
