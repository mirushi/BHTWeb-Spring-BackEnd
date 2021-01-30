package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.Doc.DocReactionStatisticDTO;
import com.bhtcnpm.website.model.dto.Doc.DocUserOwnReactionStatisticDTO;
import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionStatsDTO;
import com.bhtcnpm.website.model.entity.DocEntities.UserDocReaction;
import com.bhtcnpm.website.repository.custom.UserDocReactionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDocReactionRepository extends JpaRepository<UserDocReaction, Long>, QuerydslPredicateExecutor<UserDocReaction>, UserDocReactionRepositoryCustom {
    List<UserDocReaction> getUserDocReactionsByUserDocReactionId_UserIdAndUserDocReactionId_DocId_IdIn (Long userId, List<Long> docIds);

    @Query("SELECT new com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionStatsDTO(udr.userDocReactionId.doc.id, udr.docReactionType, COUNT(udr.userDocReactionId.user.id)) " +
            "FROM UserDocReaction udr WHERE udr.userDocReactionId.doc.id IN :docIds GROUP BY udr.userDocReactionId.doc.id, udr.docReactionType")
    List<UserDocReactionStatsDTO> getUserDocReactionsStatsDTO (List<Long> docIds);

    @Query("SELECT new com.bhtcnpm.website.model.dto.Doc.DocReactionStatisticDTO(usr.userDocReactionId.doc.id, " +
            "SUM(CASE WHEN usr.docReactionType = com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType.LIKE THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN usr.docReactionType = com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType.DISLIKE THEN 1 ELSE 0 END))" +
            "FROM UserDocReaction usr " +
            "WHERE usr.userDocReactionId.doc.id IN :docIDs " +
            "GROUP BY usr.userDocReactionId.doc.id")
    List<DocReactionStatisticDTO> getDocReactionStatisticsDTO(List<Long> docIDs);

    @Query("SELECT new com.bhtcnpm.website.model.dto.Doc.DocUserOwnReactionStatisticDTO(usr.userDocReactionId.doc.id, usr.docReactionType) " +
            "FROM UserDocReaction usr " +
            "WHERE usr.userDocReactionId.doc.id IN :docIDs AND usr.userDocReactionId.user.id = :userID ")
    List<DocUserOwnReactionStatisticDTO> getDocUserOwnReactionStatisticDTO (List<Long> docIDs, Long userID);
}
