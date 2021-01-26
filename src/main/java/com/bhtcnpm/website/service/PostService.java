package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.Post.PostStatisticDTO;

import java.util.List;

public interface PostService {

    List<PostStatisticDTO> getPostStatistic (List<Long> postIDs, Long userID);

}
