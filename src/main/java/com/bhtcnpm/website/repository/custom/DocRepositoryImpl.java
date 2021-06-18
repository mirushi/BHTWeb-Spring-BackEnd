package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.constant.business.Doc.DocBusinessConstant;
import com.bhtcnpm.website.model.dto.Doc.*;
import com.bhtcnpm.website.model.dto.Doc.mapper.DocSummaryMapper;
import com.bhtcnpm.website.model.entity.DocEntities.*;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.search.lucene.LuceneIndexUtils;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.IndexReader;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
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
    private SearchSortFactory searchSortFactory;

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
                 .select(Projections.constructor(DocSummaryDTO.class, qDoc.id, qDoc.author.id, qDoc.author.displayName, qDoc.category.id, qDoc.category.name, qDoc.subject.id, qDoc.subject.name, qDoc.title, qDoc.description, qDoc.imageURL, qDoc.publishDtm))
                 .from(qDoc)
                 .join(qUserDocReaction).on(qUserDocReaction.userDocReactionId.doc.id.eq(qDoc.id))
//                 .orderBy(qDoc.docFileUpload.downloadCount.desc())
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
    public DocSummaryListDTO getDocSummaryList(String searchTerm,
                                               Long categoryID,
                                               Long subjectID,
                                               Long authorID,
                                               DocStateType docStateType,
                                               Integer page,
                                               Integer pageSize,
                                               SortOrder sortByPublishDtm,
                                               SortOrder sortByCreatedDtm) {

        //TODO: DocState may need to depend on ACL.
        SearchResult<Doc> searchResult = getDocSearchResult(
                searchTerm,
                categoryID,
                subjectID,
                authorID,
                docStateType,
                page,
                pageSize,
                sortByPublishDtm,
                sortByCreatedDtm
        );

        Long resultCount = searchResult.total().hitCountLowerBound();

        Integer totalPages = (int)Math.ceil((double)resultCount / pageSize);

        List<DocSummaryDTO> listSummaryDTOs = docSummaryMapper.docListToDocSummaryDTOList(searchResult.hits());

        DocSummaryListDTO finalResult = new DocSummaryListDTO(listSummaryDTOs, totalPages, resultCount);

        return finalResult;
    }

    @Override
    public DocSummaryWithStateListDTO getDocSummaryWithStateList(String searchTerm,
                                                                 Long categoryID,
                                                                 Long subjectID,
                                                                 Long authorID,
                                                                 DocStateType docStateType,
                                                                 Integer page,
                                                                 Integer pageSize,
                                                                 SortOrder sortByPublishDtm,
                                                                 SortOrder sortByCreatedDtm) {
        SearchResult<Doc> searchResult = getDocSearchResult(
                searchTerm,
                categoryID,
                subjectID,
                authorID,
                docStateType,
                page,
                pageSize,
                sortByPublishDtm,
                sortByCreatedDtm
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

    private SearchResult<Doc> getDocSearchResult (String searchTerm,
                                                  Long categoryID,
                                                  Long subjectID,
                                                  Long authorID,
                                                  DocStateType docState,
                                                  Integer page,
                                                  Integer pageSize,
                                                  SortOrder sortByPublishDtm,
                                                  SortOrder sortByCreatedDtm) {
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
                    if (categoryID != null) {
                        b.filter(f.match()
                                .field("categoryID")
                                .matching(em.getReference(DocCategory.class, categoryID))
                        );
                    }
                    if (subjectID != null) {
                        b.filter(f.match()
                                .field("subjectID")
                                .matching(em.getReference(DocSubject.class, subjectID))
                        );
                    }
                    if (authorID != null) {
                        b.filter(f.match()
                                .field("authorID")
                                .matching(em.getReference(UserWebsite.class, authorID))
                        );
                    }
                    if (docState != null) {
                        b.filter(f.match()
                                .field("docState")
                                .matching(docState)
                        );
                    }
                }))
                .sort(f -> f.composite( b -> {
                    if (sortByPublishDtm != null) {
                        b.add(f.field("publishDtm").order(sortByPublishDtm));
                    }
                    if (sortByCreatedDtm != null) {
                        b.add(f.field("createdDtm").order(sortByCreatedDtm));
                    }
                }))
                .fetch(page * pageSize, pageSize);

        return searchResult;
    }

}
