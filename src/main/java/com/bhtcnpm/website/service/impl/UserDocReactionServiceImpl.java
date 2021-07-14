package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionDTO;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionMapper;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionStatsDTO;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionUserOwnDTO;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.DocEntities.UserDocReaction;
import com.bhtcnpm.website.model.entity.DocEntities.UserDocReactionId;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import com.bhtcnpm.website.model.entity.enumeration.UserWebsite.ReputationType;
import com.bhtcnpm.website.repository.Doc.DocRepository;
import com.bhtcnpm.website.repository.Doc.UserDocReactionRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Doc.DocService;
import com.bhtcnpm.website.service.UserDocReactionService;
import com.bhtcnpm.website.service.UserWebsiteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDocReactionServiceImpl implements UserDocReactionService {

    private final UserDocReactionRepository reactionRepository;

    private final UserWebsiteRepository userWebsiteRepository;

    private final UserWebsiteService userWebsiteService;

    private final DocRepository docRepository;

    private final DocService docService;

    private final UserDocReactionMapper userDocReactionMapper;

    @Override
    public List<UserDocReactionStatsDTO> getDocsStats(List<Long> docIds) {
        return reactionRepository.getUserDocReactionsStatsDTO(docIds);
    }

    @Override
    public List<UserDocReactionUserOwnDTO> getUserReactionForDocs(List<Long> docIds, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        return reactionRepository.getUserDocReactionsByUserDocReactionId_UserIdAndUserDocReactionId_DocId_IdIn(userID, docIds)
                .stream()
                .map(userDocReactionMapper::userDocReactionToUserDocReactionUserOwnDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDocReactionDTO putUserReactionForDoc(Long docID, UserDocReactionUserOwnDTO userDocReactionUserOwnDTO, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        UserDocReactionId userDocReactionId = new UserDocReactionId();

        UserWebsite user = userWebsiteRepository.getOne(userID);
        Doc doc = docRepository.getOne(docID);

        userDocReactionId.setUser(user);
        userDocReactionId.setDoc(doc);

        //Tìm xem user hiện tại đã thực hiện reaction lên Doc chưa.
        //Nếu user đã react rồi thì cập nhật reaction cũ bằng reaction mới.
        Optional<UserDocReaction> userDocReactionObject = reactionRepository.findById(userDocReactionId);
        UserDocReaction userDocReaction;
        DocReactionType oldReactionType = null;

        //Xoá reaction của User ra khỏi hệ thống nếu như User chọn là none.
        if (DocReactionType.NONE.equals(userDocReactionUserOwnDTO.getDocReactionType())) {
            if (userDocReactionObject.isPresent()) {
                userDocReaction = userDocReactionObject.get();
                oldReactionType = userDocReaction.getDocReactionType();

                reactionRepository.delete(userDocReactionObject.get());
                
                updateUserReputation(doc.getAuthor().getId(), oldReactionType, userDocReactionUserOwnDTO.getDocReactionType());
                updateDocStatistic(docID, oldReactionType, userDocReactionUserOwnDTO.getDocReactionType());
                return null;
            }
        }

        if (userDocReactionObject.isPresent()) {
            userDocReaction = userDocReactionObject.get();
            oldReactionType = userDocReaction.getDocReactionType();
        } else {
            userDocReaction = new UserDocReaction();
            userDocReaction.setUserDocReactionId(userDocReactionId);
        }
        userDocReaction.setDocReactionType(userDocReactionUserOwnDTO.getDocReactionType());
        userDocReaction = reactionRepository.save(userDocReaction);

        updateUserReputation(doc.getAuthor().getId(), oldReactionType, userDocReactionUserOwnDTO.getDocReactionType());
        updateDocStatistic(docID, oldReactionType, userDocReactionUserOwnDTO.getDocReactionType());

        return userDocReactionMapper.userDocReactionToUserDocReactionDTO(userDocReaction);
    }

    private void updateDocStatistic (Long docID, DocReactionType oldReaction, DocReactionType newReaction) {
        if (oldReaction != null && oldReaction.equals(newReaction)) {
            return;
        }
        if (DocReactionType.DISLIKE.equals(oldReaction) || DocReactionType.DISLIKE.equals(newReaction)) {
            docService.updateDownVotes(docID);
        }
        if (DocReactionType.LIKE.equals(oldReaction) || DocReactionType.LIKE.equals(newReaction)) {
            docService.updateUpVotes(docID);
        }
        docService.updateHotness(docID);
        docService.updateWilson(docID);
    }

    private void updateUserReputation (UUID userID, DocReactionType oldReaction, DocReactionType newReaction) {
        //TODO: Performance can be improved by grouping subtract and update.
        if (oldReaction != null && oldReaction.equals(newReaction)) {
            return;
        }
        if (DocReactionType.DISLIKE.equals(oldReaction)) {
            userWebsiteService.subtractUserReputationScore(userID, ReputationType.DOC_DISLIKED, 1L);
        } else if (DocReactionType.LIKE.equals(oldReaction)) {
            userWebsiteService.subtractUserReputationScore(userID, ReputationType.DOC_LIKED, 1L);
        }
        if (DocReactionType.LIKE.equals(newReaction)) {
            userWebsiteService.addUserReputationScore(userID, ReputationType.DOC_LIKED, 1L);
        } else if (DocReactionType.DISLIKE.equals(newReaction)) {
            userWebsiteService.addUserReputationScore(userID, ReputationType.DOC_DISLIKED, 1L);
        }
    }
}
