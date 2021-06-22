package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionDTO;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionStatsDTO;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionUserOwnDTO;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface UserDocReactionService {
    List<UserDocReactionStatsDTO> getDocsStats (List<Long> docIds);
    List<UserDocReactionUserOwnDTO> getUserReactionForDocs (List<Long> docIds, Authentication authentication);
    UserDocReactionDTO putUserReactionForDoc (Long docID, UserDocReactionUserOwnDTO userDocReactionUserOwnDTO, Authentication authentication);
}
