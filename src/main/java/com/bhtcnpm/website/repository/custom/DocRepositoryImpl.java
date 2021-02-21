package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.Doc.DocReactionStatisticDTO;
import com.bhtcnpm.website.model.dto.Doc.DocSummaryDTO;
import com.bhtcnpm.website.model.dto.Doc.DocSummaryListDTO;
import com.bhtcnpm.website.model.entity.DocEntities.Doc;
import com.bhtcnpm.website.model.entity.DocEntities.QDoc;
import com.bhtcnpm.website.model.entity.DocEntities.QUserDocReaction;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class DocRepositoryImpl implements DocRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

    private final QDoc qDoc = QDoc.doc;
    private final QUserDocReaction qUserDocReaction = QUserDocReaction.userDocReaction;

    private final EntityPath<Doc> path;
    private final PathBuilder<Doc> builder;

    private final Querydsl querydsl;

    public DocRepositoryImpl (EntityManager em) {
        this.em = em;
        this.path = SimpleEntityPathResolver.INSTANCE.createPath(Doc.class);
        this.builder = new PathBuilder<Doc>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(em, builder);
    }

    @Override
    public List<DocSummaryDTO> getTrendingDoc(Pageable pageable) {
         JPAQuery query = new JPAQuery<Doc>(em)
                 .select(Projections.constructor(DocSummaryDTO.class, qDoc.id, qDoc.author.id, qDoc.author.name, qDoc.category.id, qDoc.category.name, qDoc.subject.id, qDoc.subject.name, qDoc.title, qDoc.description, qDoc.imageURL, qDoc.publishDtm, qDoc.downloadCount, qDoc.viewCount, qDoc.version))
                 .from(qDoc)
                 .join(qUserDocReaction).on(qUserDocReaction.userDocReactionId.doc.id.eq(qDoc.id))
                 .orderBy(qDoc.downloadCount.desc())
                 .groupBy(qDoc);

         JPQLQuery finalQuery = querydsl.applyPagination(pageable, query);

         return finalQuery.fetch();
    }

    @Override
    public List<DocReactionStatisticDTO> getDocStatisticsWithUserID (List<Long> docIds, Long userID) {
//        JPAQuery query = new JPAQuery<Doc>(em)
//                .select(Projections.constructor(DocStatisticDTO.class, qDoc.id))
//                .from(qDoc)
//

        return null;
    }

    @Override
    public DocSummaryListDTO searchBySearchTerm(Predicate predicate, Pageable pageable, String searchTerm) {

        JPAQuery query = new JPAQuery<Post>(em)
                .select(Projections.constructor(DocSummaryDTO.class, qDoc.id, qDoc.author.id, qDoc.author.name, qDoc.category.id, qDoc.category.name,qDoc.subject.id,qDoc.subject.name, qDoc.title, qDoc.description, qDoc.imageURL, qDoc.publishDtm, qDoc.downloadCount, qDoc.viewCount, qDoc.version))
                .from(qDoc)
                .where(qDoc.title.contains(searchTerm), predicate);

        JPQLQuery finalQuery = querydsl.applyPagination(pageable, query);

        QueryResults queryResults = finalQuery.fetchResults();

        List<DocSummaryDTO> listSummaryDTOs = queryResults.getResults();

        Long resultCount = finalQuery.fetchResults().getTotal();

        Integer totalPages = (int)Math.ceil((double)resultCount / pageable.getPageSize());

        DocSummaryListDTO result = new DocSummaryListDTO(listSummaryDTOs, totalPages, resultCount);

        return result;
    }

}
