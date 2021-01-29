package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.Post.*;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;

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

    Boolean rejectPost (Long postID, Long userID);

    Boolean createSavedStatus (Long postID, Long userID);

    Boolean deleteSavedStatus (Long postID, Long userID);

    List<PostSummaryDTO> getPostWithActivityCategory();

    List<PostSummaryDTO> getPostNewest();

    PostSummaryListDTO getPostBySearchTerm (Predicate predicate, Pageable pageable, String searchTerm);
}
