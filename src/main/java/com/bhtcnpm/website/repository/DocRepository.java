package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.Doc.DocQuickSearchResult;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
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
            "SET d.isApproved = true," +
            "d.isApprovedBy.id = :userID " +
            "WHERE d.id = :docID")
    int postApprove (Long docID, Long userID);

    @Modifying
    @Query(value = "UPDATE Doc as d " +
            "SET d.isApproved = false," +
            "d.isApprovedBy.id = null " +
            "WHERE d.id = :docID")
    int deleteApprove(Long docID);

    //Please don't pass different value for searchTerm and searchTermExact.
    @Query("SELECT new com.bhtcnpm.website.model.dto.Doc.DocQuickSearchResult(d.id, d.imageURL, d.title) " +
            "FROM Doc d " +
            "WHERE d.title LIKE %:searchTerm% " +
            "ORDER BY "+ "(CASE WHEN EXISTS (SELECT 1 FROM d WHERE d.title = :searchTermExact) THEN TRUE ELSE FALSE END)" +" DESC, length(d.title)")
    List<DocQuickSearchResult> quickSearch (Pageable pageable, String searchTerm, String searchTermExact);
}
