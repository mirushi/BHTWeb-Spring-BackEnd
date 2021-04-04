package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.Doc.DocReactionStatisticDTO;
import com.bhtcnpm.website.model.dto.Doc.DocSummaryDTO;
import com.bhtcnpm.website.model.dto.Doc.DocSummaryListDTO;
import com.bhtcnpm.website.model.dto.Doc.DocSummaryWithStateListDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryListDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryWithStateListDTO;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DocRepositoryCustom {

    List<DocSummaryDTO> getTrendingDoc (Pageable pageable);

    List<DocReactionStatisticDTO> getDocStatisticsWithUserID (List<Long> docIds, Long userID);

    DocSummaryListDTO searchBySearchTerm (Predicate predicate, Pageable pageable, String searchTerm);

    DocSummaryWithStateListDTO getManagementDocs (String sortByPublishDtm,
                                                  Long docCategoryID,
                                                  Long docSubjectID,
                                                  Integer page,
                                                  Integer pageSize,
                                                  String searchTerm,
                                                  DocStateType docStateType);

}
