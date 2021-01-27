package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.Post.PostDetailsDTO;
import com.bhtcnpm.website.model.dto.Post.PostStatisticDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryListDTO;
import com.querydsl.core.types.Predicate;

import java.util.List;

public interface PostService {

    List<PostStatisticDTO> getPostStatistic (List<Long> postIDs, Long userID);

    PostSummaryListDTO getPostSummary (Predicate predicate, Integer paginator);

    PostDetailsDTO getPostDetails (Long id);
}
