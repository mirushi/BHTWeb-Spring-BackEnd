package com.bhtcnpm.website.repository.Doc;

import com.bhtcnpm.website.constant.domain.Doc.DocReactionTypeConstant;
import com.bhtcnpm.website.constant.domain.Doc.DocStateTypeConstant;
import com.bhtcnpm.website.model.dto.Doc.DocQuickSearchResult;
import com.bhtcnpm.website.model.dto.Doc.DocStatisticDTO;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.repository.Doc.custom.DocRepositoryCustom;
import org.hibernate.jpa.TypedParameterValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocRepository extends JpaRepository<Doc, Long>, QuerydslPredicateExecutor<Doc>,DocRepositoryCustom {
    @Modifying
    @Query(value = "UPDATE Doc as d " +
            "SET d.docState = :docState " +
            "WHERE d.id = :docID")
    int setDocState (Long docID, DocStateType docState);

    @Modifying
    @Query(value = "UPDATE Doc as d " +
            "SET d.docState = :docStateType, d.adminFeedback = :feedback " +
            "WHERE d.id = :docID")
    int setDocStateAndFeedback (Long docID, DocStateType docStateType, String feedback);

    //Checked OK.
    //TODO: We can save statistics directly in entity to optimize performance.
    @Query(nativeQuery = true, value = "SELECT d.ID AS id, COUNT(DISTINCT dc.ID) as commentCount, " +
            "COUNT(DISTINCT CASE WHEN reaction.DOC_REACTION_TYPE = 0 THEN reaction.USER_ID END) AS likeCount, " +
            "COUNT(DISTINCT CASE WHEN reaction.DOC_REACTION_TYPE = 1 THEN reaction.USER_ID END) AS dislikeCount, " +
            "COUNT(DISTINCT dv.id) AS viewCount, " +
            "COUNT(DISTINCT dl.ID) AS downloadCount, " +
            "userOwnReaction.DOC_REACTION_TYPE as docReactionType, " +
            "CASE WHEN COUNT(CASE WHEN uds.USER_ID = :userID THEN 1 END) > 0 THEN TRUE ELSE FALSE END AS savedStatus " +
            "FROM DOC d LEFT JOIN (SELECT subReaction.DOC_ID, subReaction.DOC_REACTION_TYPE FROM USER_DOC_REACTION subReaction WHERE subReaction.USER_ID = :userID) userOwnReaction ON userOwnReaction.DOC_ID = d.ID " +
            "LEFT JOIN USER_DOC_REACTION reaction ON d.ID = reaction.DOC_ID " +
            "LEFT JOIN DOC_COMMENT dc ON d.ID = dc.DOC_ID " +
            "LEFT JOIN DOC_VIEW dv ON d.ID = dv.DOC_ID " +
            "LEFT JOIN DOC_FILE_UPLOAD file ON d.ID = file.DOC_ID " +
            "LEFT JOIN DOC_DOWNLOAD dl ON dl.DOC_FILE_UPLOAD_ID = file.ID " +
            "LEFT JOIN USER_DOC_SAVE uds ON d.ID = uds.DOC_ID " +
            "WHERE d.ID IN :docIDs AND d.DELETED_DTM IS NULL " +
            "GROUP BY d.ID, userOwnReaction.DOC_REACTION_TYPE")
    List<DocStatisticDTO> getDocStatisticDTOs (List<Long> docIDs, @Param("userID")TypedParameterValue userID);

//    @Modifying
//    @Query("UPDATE Doc d SET d.docFileUpload.downloadCount = d.docFileUpload.downloadCount + 1 WHERE d.id = :docID")
//    int incrementDownloadCount(Long docID);

    Page<Doc> findByDocState (Pageable pageable, DocStateType docStateType);

    Page<Doc> findByAuthorId (Pageable pageable, Long authorID);

    @EntityGraph(value = "tagsAndDocFileUploads.all")
    Optional<Doc> getAllFilterWithTagsAndDocFileUploadsById(Long id);

    //Checked ok.
    @Query(nativeQuery = true,
            value = "SELECT DOC.* " +
            "FROM DOC " +
            "WHERE DOC.DOC_STATE = "+ DocStateTypeConstant.APPROVED_ORDINAL +" AND DELETED_DTM IS NULL AND DOC.PUBLISH_DTM <= CURRENT_TIMESTAMP " +
            "ORDER BY HOTNESS DESC")
    //Reddit Hot Algorithm (https://www.evanmiller.org/deriving-the-reddit-formula.html).
    List<Doc> getHotDocsPublicOnly(Pageable pageable);
}
