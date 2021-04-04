package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.constant.business.Doc.DocBusinessConstant;
import com.bhtcnpm.website.constant.business.GenericBusinessConstant;
import com.bhtcnpm.website.model.dto.Doc.*;
import com.bhtcnpm.website.model.entity.DocEntities.*;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.search.lucene.LuceneIndexUtils;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.IndexReader;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.List;

@Component
public class DocRepositoryImpl implements DocRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

    private final QDoc qDoc = QDoc.doc;
    private final QUserDocReaction qUserDocReaction = QUserDocReaction.userDocReaction;

    private final EntityPath<Doc> path;
    private final PathBuilder<Doc> builder;

    private final SearchSession searchSession;

    private final IndexReader luceneIndexReader;

    private final DocSummaryMapper docSummaryMapper;

    private final Querydsl querydsl;

    public DocRepositoryImpl (EntityManager em, DocSummaryMapper docSummaryMapper) throws IOException {
        this.em = em;
        this.path = SimpleEntityPathResolver.INSTANCE.createPath(Doc.class);
        this.builder = new PathBuilder<Doc>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(em, builder);
        this.docSummaryMapper = docSummaryMapper;

        this.luceneIndexReader = LuceneIndexUtils.getReader("Doc");
        this.searchSession = Search.session(em);
    }

    @Override
    public List<DocSummaryDTO> getTrendingDoc(Pageable pageable) {
         JPAQuery query = new JPAQuery<Doc>(em)
                 .select(Projections.constructor(DocSummaryDTO.class, qDoc.id, qDoc.author.id, qDoc.author.name, qDoc.category.id, qDoc.category.name, qDoc.subject.id, qDoc.subject.name, qDoc.title, qDoc.description, qDoc.imageURL, qDoc.publishDtm, qDoc.docFileUpload.downloadCount, qDoc.viewCount, qDoc.version))
                 .from(qDoc)
                 .join(qUserDocReaction).on(qUserDocReaction.userDocReactionId.doc.id.eq(qDoc.id))
                 .orderBy(qDoc.docFileUpload.downloadCount.desc())
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
                .select(Projections.constructor(DocSummaryDTO.class, qDoc.id, qDoc.author.id, qDoc.author.name, qDoc.category.id, qDoc.category.name,qDoc.subject.id,qDoc.subject.name, qDoc.title, qDoc.description, qDoc.imageURL, qDoc.publishDtm, qDoc.docFileUpload.downloadCount, qDoc.viewCount, qDoc.version))
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

    @Override
    public DocSummaryWithStateListDTO getManagementDocs(String sortByPublishDtm,
                                                        Long docCategoryID,
                                                        Long docSubjectID,
                                                        Integer page,
                                                        Integer pageSize,
                                                        String searchTerm,
                                                        DocStateType docStateType) {
        SearchResult<Doc> searchResult = getDocSearchResult(
                sortByPublishDtm,
                docCategoryID,
                docSubjectID,
                page,
                pageSize,
                searchTerm,
                docStateType
        );

        Long resultCount = searchResult.total().hitCountLowerBound();

        Integer totalPages = (int)Math.ceil((double)resultCount / pageSize);

        List<DocSummaryWithStateDTO> docSummaryDTOs = docSummaryMapper.docListToDocSummaryWithStateDTOList(searchResult.hits());

        DocSummaryWithStateListDTO finalResult = new DocSummaryWithStateListDTO(
                docSummaryDTOs,
                totalPages,
                resultCount
        );

        return finalResult;
    }

    private SearchResult<Doc> getDocSearchResult (String sortByPublishDtm,
                                                  Long docCategoryID,
                                                  Long docSubjectID,
                                                  Integer page,
                                                  Integer pageSize,
                                                  String searchTerm,
                                                  DocStateType docStateType) {
        SearchResult<Doc> searchResult = searchSession.search(Doc.class)
                .where(f -> f.bool(b -> {
                    b.filter(f.matchAll());
                    if (StringUtils.isNotEmpty(searchTerm)) {
                        b.must(f.match()
                                .field("title").boost(DocBusinessConstant.SEARCH_TITLE_BOOST)
                                .field("description").boost(DocBusinessConstant.SEARCH_DESCRIPTION_BOOST)
                                .matching(searchTerm)
                        );
                    }
                    if (docCategoryID != null) {
                        b.filter(f.match()
                                .field("categoryID")
                                .matching(em.getReference(DocCategory.class, docCategoryID))
                        );
                    }
                    if (docSubjectID != null) {
                        b.filter(f.match()
                                .field("subjectID")
                                .matching(em.getReference(DocSubject.class, docSubjectID))
                        );
                    }
                    if (docStateType != null) {
                        b.filter(f.match()
                                .field("docState")
                                .matching(docStateType)
                        );
                    }
                }))
                .sort(f -> f.composite( b -> {
                    if (sortByPublishDtm != null) {
                        if (GenericBusinessConstant.SORT_ASC.equalsIgnoreCase(sortByPublishDtm)) {
                            b.add(f.field("publishDtm").asc());
                        } else if (GenericBusinessConstant.SORT_DESC.equalsIgnoreCase(sortByPublishDtm)) {
                            b.add(f.field("publishDtm").desc());
                        }
                    }
                }))
                .fetch(page * pageSize, pageSize);

        return searchResult;
    }

}
