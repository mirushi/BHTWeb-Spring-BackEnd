package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionDTO;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionStatsDTO;
import com.bhtcnpm.website.model.entity.UserDocReaction;

import java.util.List;
import java.util.Map;

public interface UserDocReactionRepositoryCustom {

    Map<Long, Long> getUserDocReactionStatsDTO (List<Long> docIDs);

}
