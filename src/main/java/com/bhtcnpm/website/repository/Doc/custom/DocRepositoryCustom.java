package com.bhtcnpm.website.repository.Doc.custom;

import com.bhtcnpm.website.constant.ApiSortOrder;
import com.bhtcnpm.website.constant.domain.Doc.DocBusinessState;
import com.bhtcnpm.website.model.dto.Doc.*;
import com.bhtcnpm.website.model.dto.Post.PostSummaryListDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryWithStateListDTO;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.querydsl.core.types.Predicate;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
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

    DocSummaryWithStateListDTO getMyDocSummaryWithStateList (String searchTerm,
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
    void indexDoc (Long docID);
    void indexDoc (Doc doc);
    void removeIndexDoc (Long docID);
    void removeIndexDoc (Doc doc);
}
