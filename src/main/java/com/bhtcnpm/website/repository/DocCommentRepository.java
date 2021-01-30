package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.dto.Doc.DocCommentStatisticDTO;
import com.bhtcnpm.website.model.entity.DocEntities.DocComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocCommentRepository extends JpaRepository<DocComment, Long> {
    Page<DocComment> findAllByDocId (Pageable pageable, Long docID);
    Page<DocComment> findAllByDocIdAndParentCommentIsNull (Pageable pageable, Long docID);

    @Query("SELECT new com.bhtcnpm.website.model.dto.Doc.DocCommentStatisticDTO(dc.doc.id, COUNT(dc.id)) " +
            "FROM DocComment dc " +
            "WHERE dc.doc.id IN :docIDs " +
            "GROUP BY dc.doc.id " +
            "ORDER BY dc.doc.id ASC ")
    List<DocCommentStatisticDTO> getDocCommentStatistic (List<Long> docIDs);
}
