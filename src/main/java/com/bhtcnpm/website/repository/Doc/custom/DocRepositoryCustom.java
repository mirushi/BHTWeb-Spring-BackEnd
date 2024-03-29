package com.bhtcnpm.website.repository.Doc.custom;

import com.bhtcnpm.website.constant.domain.Doc.DocBusinessState;
import com.bhtcnpm.website.constant.sort.AdvancedSort;
import com.bhtcnpm.website.model.dto.Doc.*;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.querydsl.core.types.Predicate;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface DocRepositoryCustom {

    List<DocSummaryDTO> getTrendingDoc (Pageable pageable);

    List<DocReactionStatisticDTO> getDocStatisticsWithUserID (List<Long> docIds, Long userID);

    DocSummaryListDTO getDocSummaryList (String searchTerm,
                                         String tagContent,
                                         Long categoryID,
                                         Long subjectID,
                                         UUID authorID,
                                         DocStateType docStateType,
                                         Integer page,
                                         Integer pageSize,
                                         SortOrder sortByPublishDtm,
                                         SortOrder sortByCreatedDtm,
                                         AdvancedSort advancedSort,
                                         Authentication authentication);

    DocSummaryWithStateListDTO getDocSummaryWithStateList(String searchTerm,
                                                          String tagContent,
                                                          Long categoryID,
                                                          Long subjectID,
                                                          UUID authorID,
                                                          DocStateType docStateType,
                                                          Integer page,
                                                          Integer pageSize,
                                                          SortOrder sortByPublishDtm,
                                                          SortOrder sortByCreatedDtm,
                                                          Authentication authentication);

    DocSummaryWithStateAndFeedbackListDTO getMyDocSummaryWithStateList (String searchTerm,
                                                             String tagContent,
                                                             Long categoryID,
                                                             Long subjectID,
                                                             UUID authorID,
                                                             DocStateType docStateType,
                                                             Integer page,
                                                             Integer pageSize,
                                                             SortOrder sortByPublishDtm,
                                                             SortOrder sortByCreatedDtm);
    List<DocSuggestionDTO> searchRelatedDocs (UUID authorID, Long categoryID, Long subjectID, Long currentDocID, String title, String description,
                                              int page, int pageSize, DocBusinessState docBusinessState, Authentication authentication) throws IOException;
    Page<Doc> getDocOrderByViewCountDESC (Predicate predicate, Pageable pageable);
    Page<Doc> getDocOrderByLikeCountDESC (Predicate predicate, Pageable pageable);
    Page<Doc> getDocOrderByDownloadCountDESC (Predicate predicate, Pageable pageable);
    Page<Doc> quickSearch (Pageable pageable, String searchTerm);
    void indexDoc (Long docID);
    void indexDoc (Doc doc);
    void removeIndexDoc (Long docID);
    void removeIndexDoc (Doc doc);
}
