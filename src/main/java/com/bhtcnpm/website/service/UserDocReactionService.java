package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionDTO;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionStatsDTO;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionUserOwnDTO;

import java.util.List;
import java.util.UUID;

public interface UserDocReactionService {
    List<UserDocReactionStatsDTO> getDocsStats (List<Long> docIds);
    List<UserDocReactionUserOwnDTO> getUserReactionForDocs (Long userID, List<Long> docIds);
    UserDocReactionDTO putUserReactionForDoc (UUID userID, UserDocReactionUserOwnDTO userDocReactionUserOwnDTO);
}
