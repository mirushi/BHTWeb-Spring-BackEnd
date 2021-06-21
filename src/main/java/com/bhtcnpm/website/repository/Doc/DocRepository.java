package com.bhtcnpm.website.repository.Doc;

import com.bhtcnpm.website.constant.domain.Doc.DocReactionTypeConstant;
import com.bhtcnpm.website.constant.domain.Doc.DocStateTypeConstant;
import com.bhtcnpm.website.model.dto.Doc.DocQuickSearchResult;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.repository.Doc.custom.DocRepositoryCustom;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @EntityGraph(value = "tagsAndDocFileUploads.all")
    Optional<Doc> getAllFilterWithTagsAndDocFileUploadsById(Long id);

    @Query(nativeQuery = true,
            value = "SELECT DOC.* " +
            "FROM DOC " +
            "LEFT JOIN USER_DOC_REACTION AS REACTION ON DOC.ID = REACTION.DOC_ID " +
            "WHERE REACTION.DOC_REACTION_TYPE = "+ DocStateTypeConstant.APPROVED_ORDINAL +" AND DELETED_DATE IS NULL AND DOC.PUBLISH_DTM <= CURRENT_TIMESTAMP " +
            "GROUP BY DOC.ID " +
            "ORDER BY ROUND(((CASE WHEN COUNT(DISTINCT (CASE WHEN REACTION.DOC_REACTION_TYPE = "+ DocReactionTypeConstant.LIKE_ORDINAL +" THEN REACTION.USER_ID ELSE NULL END)) - COUNT(DISTINCT (CASE WHEN REACTION.DOC_REACTION_TYPE = "+ DocReactionTypeConstant.DISLIKE_ORDINAL +" THEN REACTION.USER_ID ELSE null END)) > 0 THEN 1 WHEN COUNT(DISTINCT (CASE WHEN REACTION.DOC_REACTION_TYPE = "+ DocReactionTypeConstant.LIKE_ORDINAL +" THEN REACTION.USER_ID ELSE NULL END)) - COUNT(DISTINCT (CASE WHEN REACTION.DOC_REACTION_TYPE = "+ DocReactionTypeConstant.DISLIKE_ORDINAL +" THEN REACTION.USER_ID ELSE null END)) < 0 THEN -1 ELSE 0 END) * (LOG(GREATEST(ABS(COUNT(DISTINCT (CASE WHEN REACTION.DOC_REACTION_TYPE = "+ DocReactionTypeConstant.LIKE_ORDINAL +" THEN REACTION.USER_ID ELSE NULL END)) - COUNT(DISTINCT (CASE WHEN REACTION.DOC_REACTION_TYPE = "+ DocReactionTypeConstant.DISLIKE_ORDINAL +" THEN REACTION.USER_ID ELSE null END))), 1))) + ((EXTRACT(EPOCH FROM DOC.PUBLISH_DTM) - 1446422400)/45000)), 7) DESC")
    List<Doc> getHotDocsPublicOnly(Pageable pageable);
}
