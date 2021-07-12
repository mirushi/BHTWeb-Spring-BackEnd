package com.bhtcnpm.website.service;

import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionDTO;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionStatsDTO;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionUserOwnDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserDocReactionService {
    @PreAuthorize(value = "permitAll()")
    List<UserDocReactionStatsDTO> getDocsStats (List<Long> docIds);
    @PreAuthorize(value = "permitAll()")
    List<UserDocReactionUserOwnDTO> getUserReactionForDocs (List<Long> docIds, Authentication authentication);
    @PreAuthorize(value = "hasPermission(#docID, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.ObjectTypeConstant).DOC_OBJECT, " +
            "T(com.bhtcnpm.website.constant.security.evaluator.permission.DocActionPermissionRequest).REACT_PERMISSION)")
    UserDocReactionDTO putUserReactionForDoc (Long docID, UserDocReactionUserOwnDTO userDocReactionUserOwnDTO, Authentication authentication);
}
