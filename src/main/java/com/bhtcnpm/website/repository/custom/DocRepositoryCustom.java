package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.constant.ApiSortOrder;
import com.bhtcnpm.website.model.dto.Doc.DocReactionStatisticDTO;
import com.bhtcnpm.website.model.dto.Doc.DocSummaryDTO;
import com.bhtcnpm.website.model.dto.Doc.DocSummaryListDTO;
import com.bhtcnpm.website.model.dto.Doc.DocSummaryWithStateListDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryListDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryWithStateListDTO;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.querydsl.core.types.Predicate;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DocRepositoryCustom {

    List<DocSummaryDTO> getTrendingDoc (Pageable pageable);

    List<DocReactionStatisticDTO> getDocStatisticsWithUserID (List<Long> docIds, Long userID);

    DocSummaryListDTO searchBySearchTerm (String searchTerm,
                                          Integer page,
                                          Integer pageSize,
                                          SortOrder sortByPublishDtm,
                                          Long categoryID,
                                          Long subjectID);

    DocSummaryWithStateListDTO getManagementDocs (SortOrder sortByPublishDtm,
                                                  Long categoryID,
                                                  Long subjectID,
                                                  Integer page,
                                                  Integer pageSize,
                                                  String searchTerm,
                                                  DocStateType docStateType);

}
