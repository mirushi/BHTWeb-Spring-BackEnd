package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionDTO;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionMapper;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionStatsDTO;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionUserOwnDTO;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.DocEntities.UserDocReaction;
import com.bhtcnpm.website.model.entity.DocEntities.UserDocReactionId;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.repository.DocRepository;
import com.bhtcnpm.website.repository.UserDocReactionRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.service.UserDocReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDocReactionServiceImpl implements UserDocReactionService {

    private final UserDocReactionRepository reactionRepository;

    private final UserWebsiteRepository userWebsiteRepository;

    private final DocRepository docRepository;

    private final UserDocReactionMapper userDocReactionMapper;

    @Override
    public List<UserDocReactionStatsDTO> getDocsStats(List<Long> docIds) {
        return reactionRepository.getUserDocReactionsStatsDTO(docIds);
    }

    @Override
    public List<UserDocReactionUserOwnDTO> getUserReactionForDocs(Long userID, List<Long> docIds) {
        return reactionRepository.getUserDocReactionsByUserDocReactionId_UserIdAndUserDocReactionId_DocId_IdIn(userID, docIds)
                .stream()
                .map(userDocReactionMapper::userDocReactionToUserDocReactionUserOwnDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDocReactionDTO putUserReactionForDoc(Long userID, UserDocReactionUserOwnDTO userDocReactionUserOwnDTO) {
        UserDocReactionId userDocReactionId = new UserDocReactionId();

        UserWebsite user = userWebsiteRepository.getOne(userID);
        Doc doc = docRepository.getOne(userDocReactionUserOwnDTO.getDocID());

        userDocReactionId.setUser(user);
        userDocReactionId.setDoc(doc);

        UserDocReaction userDocReaction = new UserDocReaction();
        userDocReaction.setUserDocReactionId(userDocReactionId);
        userDocReaction.setDocReactionType(userDocReactionUserOwnDTO.getDocReactionType());

        userDocReaction = reactionRepository.save(userDocReaction);

        return userDocReactionMapper.userDocReactionToUserDocReactionDTO(userDocReaction);
    }
}
