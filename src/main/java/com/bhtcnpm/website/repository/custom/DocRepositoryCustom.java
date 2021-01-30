package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.Doc.DocReactionStatisticDTO;
import com.bhtcnpm.website.model.dto.Doc.DocSummaryDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DocRepositoryCustom {

    List<DocSummaryDTO> getTrendingDoc (Pageable pageable);

    List<DocReactionStatisticDTO> getDocStatisticsWithUserID (List<Long> docIds, Long userID);

}
