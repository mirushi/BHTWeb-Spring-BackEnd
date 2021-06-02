package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.Post.*;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface PostRepositoryCustom {
    PostSummaryListDTO searchBySearchTerm (String sortByPublishDtm, Long postCategoryID ,Integer page, Integer pageSize ,String searchTerm);
    PostSummaryWithStateListDTO getManagementPost (String sortByPublishDtm,
                                                   Long postCategoryID,
                                                   Integer page,
                                                   Integer pageSize,
                                                   String searchTerm,
                                                   PostStateType postStateType);
    List<PostSuggestionDTO> searchRelatedPost(UUID authorID, Long categoryID, Post entity, int page , int pageSize) throws IOException;
    PostSummaryWithStateListDTO searchBySearchTermWithState (Predicate predicate, Pageable pageable);
    PostSummaryWithStateAndFeedbackListDTO getPostSummaryStateFeedback (Predicate predicate, Pageable pageable);
    PostDetailsWithStateListDTO getPostDetailsListWithStateFilter(Predicate predicate, Pageable pageable, PostStateType postStateType);
    List<PostQuickSearchResult> quickSearch (int page, int pageSize, String searchTerm);
}
