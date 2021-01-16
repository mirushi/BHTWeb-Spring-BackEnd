package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionStatsDTO;
import com.bhtcnpm.website.model.entity.UserDocReaction;
import com.bhtcnpm.website.repository.custom.UserDocReactionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Set;

public interface UserDocReactionRepository extends JpaRepository<UserDocReaction, Long>, QuerydslPredicateExecutor<UserDocReaction>, UserDocReactionRepositoryCustom {
    List<UserDocReaction> getUserDocReactionsByUserDocReactionId_UserIdAndUserDocReactionId_DocId_IdIn (Long userId, List<Long> docIds);

    @Query("SELECT new com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionStatsDTO(udr.userDocReactionId.doc.id, udr.docReactionType, COUNT(udr.userDocReactionId.user.id)) " +
            "FROM UserDocReaction udr WHERE udr.userDocReactionId.doc.id IN :docIds GROUP BY udr.userDocReactionId.doc.id, udr.docReactionType")
    List<UserDocReactionStatsDTO> getUserDocReactionsStatsDTO (List<Long> docIds);
}
