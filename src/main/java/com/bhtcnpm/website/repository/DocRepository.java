package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.Doc.DocQuickSearchResult;
import com.bhtcnpm.website.model.dto.Doc.DocReactionStatisticDTO;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.repository.custom.DocRepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocRepository extends JpaRepository<Doc, Long>, QuerydslPredicateExecutor<Doc>,DocRepositoryCustom {
    @Modifying
    @Query(value = "UPDATE Doc as d " +
            "SET d.docState = :docState " +
            "WHERE d.id = :docID")
    int setDocState (Long docID, DocStateType docState);

    //Please don't pass different value for searchTerm and searchTermExact.
    @Query("SELECT new com.bhtcnpm.website.model.dto.Doc.DocQuickSearchResult(d.id, d.imageURL, d.title) " +
            "FROM Doc d " +
            "WHERE d.title LIKE %:searchTerm% " +
            "ORDER BY "+ "(CASE WHEN EXISTS (SELECT 1 FROM d WHERE d.title = :searchTermExact) THEN TRUE ELSE FALSE END)" +" DESC, length(d.title)")
    List<DocQuickSearchResult> quickSearch (Pageable pageable, String searchTerm, String searchTermExact);

    @Modifying
    @Query("UPDATE Doc d SET d.downloadCount = d.downloadCount + 1 WHERE d.id = :docID")
    int incrementDownloadCount(Long docID);

    List<Doc> getDocByIdNot (Pageable pageable, Long docID);

    @Query("SELECT new com.bhtcnpm.website.model.dto.Doc.DocReactionStatisticDTO(usr.userDocReactionId.doc.id, " +
            "SUM(CASE WHEN usr.docReactionType = com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType.LIKE THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN usr.docReactionType = com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType.DISLIKE THEN 1 ELSE 0 END)," +
            "CASE WHEN EXISTS (SELECT 1 FROM UserDocReaction subUsr " +
            "WHERE subUsr.userDocReactionId.doc.id = usr.userDocReactionId.doc.id AND subUsr.userDocReactionId.user.id = :userID) THEN 1 ELSE 0 END)" +
            "FROM UserDocReaction usr " +
            "WHERE usr.userDocReactionId.doc.id IN :docIDs " +
            "GROUP BY usr.userDocReactionId.doc.id")
    List<DocReactionStatisticDTO> getDocReactionStatisticsDTO(List<Long> docIDs, Long userID);

}
