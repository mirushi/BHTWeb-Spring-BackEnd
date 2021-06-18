package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.Doc.DocQuickSearchResult;
import com.bhtcnpm.website.model.dto.Doc.DocReactionStatisticDTO;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.repository.custom.DocRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

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

//    @Modifying
//    @Query("UPDATE Doc d SET d.docFileUpload.downloadCount = d.docFileUpload.downloadCount + 1 WHERE d.id = :docID")
//    int incrementDownloadCount(Long docID);

    List<Doc> getDocByIdNot (Pageable pageable, Long docID);

    Page<Doc> findByDocState (Pageable pageable, DocStateType docStateType);

    Page<Doc> findByAuthorId (Pageable pageable, Long authorID);
}
