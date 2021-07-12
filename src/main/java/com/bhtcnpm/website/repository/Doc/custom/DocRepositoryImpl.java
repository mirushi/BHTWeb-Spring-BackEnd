package com.bhtcnpm.website.repository.Doc.custom;

import com.bhtcnpm.website.constant.business.Doc.DocBusinessConstant;
import com.bhtcnpm.website.constant.domain.Doc.DocBusinessState;
import com.bhtcnpm.website.model.dto.Doc.*;
import com.bhtcnpm.website.model.dto.Doc.mapper.DocSuggestionMapper;
import com.bhtcnpm.website.model.dto.Doc.mapper.DocSummaryMapper;
import com.bhtcnpm.website.model.entity.DocEntities.*;
import com.bhtcnpm.website.model.entity.SubjectEntities.Subject;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.DocReaction.DocReactionType;
import com.bhtcnpm.website.model.entity.enumeration.DocState.DocStateType;
import com.bhtcnpm.website.search.lucene.LuceneIndexUtils;
import com.bhtcnpm.website.security.predicate.Doc.DocHibernateSearchPredicateGenerator;
import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.Query;
import org.hibernate.search.backend.lucene.LuceneExtension;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.dsl.SearchSortFactory;
import org.hibernate.search.engine.search.sort.dsl.SortOrder;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.scope.SearchScope;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.orm.work.SearchIndexingPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Expression;
import java.io.IOException;
import java.util.*;

@Component
public class DocRepositoryImpl implements DocRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

    private final QDoc qDoc = QDoc.doc;
    private final QDocFileUpload qDocFileUpload = QDocFileUpload.docFileUpload;
    private final QDocView qDocView = QDocView.docView;
    private final QDocDownload qDocDownload = QDocDownload.docDownload;
    private final QUserDocReaction qUserDocReaction = QUserDocReaction.userDocReaction;

    private final EntityPath<Doc> path;
    private final PathBuilder<Doc> builder;

    private final SearchSession searchSession;

    private final IndexReader luceneIndexReader;

    private final DocSummaryMapper docSummaryMapper;

    private final DocSuggestionMapper docSuggestionMapper;

    private final Querydsl querydsl;
    private SearchSortFactory searchSortFactory;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    public DocRepositoryImpl (EntityManager em, DocSummaryMapper docSummaryMapper, DocSuggestionMapper docSuggestionMapper, CriteriaBuilderFactory criteriaBuilderFactory) throws IOException {
        this.em = em;
        this.path = SimpleEntityPathResolver.INSTANCE.createPath(Doc.class);
        this.builder = new PathBuilder<Doc>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(em, builder);
        this.docSummaryMapper = docSummaryMapper;
        this.docSuggestionMapper = docSuggestionMapper;

        this.luceneIndexReader = LuceneIndexUtils.getReader("Doc");
        this.searchSession = Search.session(em);
        this.criteriaBuilderFactory = criteriaBuilderFactory;
    }

    @Override
    public List<DocSummaryDTO> getTrendingDoc(Pageable pageable) {
         JPAQuery<DocSummaryDTO> query = new JPAQuery<Doc>(em)
                 .select(Projections.constructor(DocSummaryDTO.class, qDoc.id, qDoc.author.id, qDoc.author.displayName, qDoc.category.id, qDoc.category.name, qDoc.subject.id, qDoc.subject.name, qDoc.title, qDoc.description, qDoc.imageURL, qDoc.publishDtm))
                 .from(qDoc)
                 .join(qUserDocReaction).on(qUserDocReaction.userDocReactionId.doc.id.eq(qDoc.id))
//                 .orderBy(qDoc.docFileUpload.downloadCount.desc())
                 .groupBy(qDoc);

         JPQLQuery<DocSummaryDTO> finalQuery = querydsl.applyPagination(pageable, query);

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
                                               String tagContent,
                                               Long categoryID,
                                               Long subjectID,
                                               UUID authorID,
                                               DocStateType docStateType,
                                               Integer page,
                                               Integer pageSize,
                                               SortOrder sortByPublishDtm,
                                               SortOrder sortByCreatedDtm,
                                               Authentication authentication) {

        SearchResult<Doc> searchResult = getDocSearchResult(
                searchTerm,
                tagContent,
                categoryID,
                subjectID,
                authorID,
                docStateType,
                page,
                pageSize,
                sortByPublishDtm,
                sortByCreatedDtm,
                DocBusinessState.PUBLIC,
                authentication,
                false
        );

        Long resultCount = searchResult.total().hitCountLowerBound();

        Integer totalPages = (int)Math.ceil((double)resultCount / pageSize);

        List<DocSummaryDTO> listSummaryDTOs = docSummaryMapper.docListToDocSummaryDTOList(searchResult.hits());

        DocSummaryListDTO finalResult = new DocSummaryListDTO(listSummaryDTOs, totalPages, resultCount);

        return finalResult;
    }

    @Override
    public DocSummaryWithStateListDTO getDocSummaryWithStateList(String searchTerm,
                                                                 String tagContent,
                                                                 Long categoryID,
                                                                 Long subjectID,
                                                                 UUID authorID,
                                                                 DocStateType docStateType,
                                                                 Integer page,
                                                                 Integer pageSize,
                                                                 SortOrder sortByPublishDtm,
                                                                 SortOrder sortByCreatedDtm,
                                                                 Authentication authentication) {
        SearchResult<Doc> searchResult = getDocSearchResult(
                searchTerm,
                tagContent,
                categoryID,
                subjectID,
                authorID,
                docStateType,
                page,
                pageSize,
                sortByPublishDtm,
                sortByCreatedDtm,
                null,
                authentication,
                false
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

    @Override
    public DocSummaryWithStateAndFeedbackListDTO getMyDocSummaryWithStateList (String searchTerm,
                                                                    String tagContent,
                                                                    Long categoryID,
                                                                    Long subjectID,
                                                                    UUID authorID,
                                                                    DocStateType docStateType,
                                                                    Integer page,
                                                                    Integer pageSize,
                                                                    SortOrder sortByPublishDtm,
                                                                    SortOrder sortByCreatedDtm) {
        SearchResult<Doc> searchResult = getDocSearchResult(
                searchTerm,
                tagContent,
                categoryID,
                subjectID,
                authorID,
                docStateType,
                page,
                pageSize,
                sortByPublishDtm,
                sortByCreatedDtm,
                null,
                null,
                true
        );

        Long resultCount = searchResult.total().hitCountLowerBound();

        Integer totalPages = (int)Math.ceil((double)resultCount / pageSize);

        List<DocSummaryWithStateAndFeedbackDTO> docSummaryDTOs = docSummaryMapper.docListToDocSummaryWithStateAndFeedbackDTOList(searchResult.hits());

        DocSummaryWithStateAndFeedbackListDTO finalResult = DocSummaryWithStateAndFeedbackListDTO.builder()
                .docSummaryWithStateAndFeedbackDTOs(docSummaryDTOs)
                .totalElements(resultCount)
                .totalPages(totalPages)
                .build();

        return finalResult;
    }

    @Override
    public List<DocSuggestionDTO> searchRelatedDocs(UUID authorID, Long categoryID, Long subjectID, Long currentDocID, String title, String description, int page, int pageSize, DocBusinessState docBusinessState, Authentication authentication) throws IOException {
        MoreLikeThis mlt = new MoreLikeThis(luceneIndexReader);
        mlt.setFieldNames(new String[]{"title", "description"});
        mlt.setMinTermFreq(0);
        mlt.setMinDocFreq(0);
        mlt.setAnalyzer(LuceneIndexUtils.getStandardAnalyzer());

        Map<String, Collection<Object>> filteredDocument = new HashMap<>();
        filteredDocument.put("title", Collections.singletonList(title));
        filteredDocument.put("description", Collections.singletonList(description));

        Query query = mlt.like(filteredDocument);

        SearchScope<Doc> scope = getSearchScope();

        SearchPredicate docBusinessStatePredicate = DocHibernateSearchPredicateGenerator.getSearchPredicateOnBusinessState(docBusinessState, scope);
        SearchPredicate authorizationPredicate = DocHibernateSearchPredicateGenerator.getSearchPredicateOnAuthentication(authentication, scope);

        SearchResult<Doc> searchResults = searchSession.search(Doc.class)
                .extension(LuceneExtension.get())
                .where(f -> f.bool(b -> {
                    b.must(f.fromLuceneQuery(query));
                    b.must(docBusinessStatePredicate);
                    b.must(authorizationPredicate);
                    if (currentDocID != null) {
                        b.mustNot(f.match()
                                .field("id")
                                .matching(currentDocID));
                    }
                    if (authorID != null) {
                        b.must(f.match()
                                .field("authorID")
                                .matching(em.getReference(UserWebsite.class, authorID)));
                    }
                    if (categoryID != null) {
                        b.must(f.match()
                                .field("categoryID")
                                .matching(em.getReference(DocCategory.class, categoryID)));
                    }
                })).fetch(page * pageSize, pageSize);

        List<Doc> hits = searchResults.hits();

        return docSuggestionMapper.docListToDocSuggestionListDTO(hits);
    }

    @Override
    public Page<Doc> getDocOrderByViewCountDESC(Predicate predicate, Pageable pageable) {
        JPAQuery<Doc> query = new JPAQuery<>(em)
                .select(qDoc)
                .from(qDoc)
                .leftJoin(qDocView).on(qDoc.id.eq(qDocView.doc.id))
                .where(predicate)
                .orderBy(qDocView.id.countDistinct().desc());
        query = groupByDoc(query);
        query = applyPaginatorPageOnly(query, pageable);

        Long totalElements = getDocTotalElements(predicate);

        return new PageImpl<>(query.fetch(), pageable, totalElements);
    }

    @Override
    public Page<Doc> getDocOrderByLikeCountDESC(Predicate predicate, Pageable pageable) {
        NumberExpression<Long> countLikeOnly = new CaseBuilder()
                .when(qUserDocReaction.docReactionType.eq(DocReactionType.LIKE))
                .then(1L)
                .otherwise(0L);

        NumberExpression<Long> countDislikeOnly = new CaseBuilder()
                .when(qUserDocReaction.docReactionType.eq(DocReactionType.DISLIKE))
                .then(1L)
                .otherwise(0L);

        JPAQuery<Doc> query = new JPAQuery<>(em)
                .select(qDoc)
                .from(qDoc)
                .leftJoin(qUserDocReaction).on(qUserDocReaction.userDocReactionId.doc.id.eq(qDoc.id))
                .where(predicate)
                .orderBy(countLikeOnly.sum().subtract(countDislikeOnly.sum()).desc());
        query = groupByDoc(query);
        query = applyPaginatorPageOnly(query, pageable);

        long totalElements = getDocTotalElements(predicate);

        return new PageImpl<>(query.fetch(), pageable, totalElements);
    }

    @Override
    public Page<Doc> getDocOrderByDownloadCountDESC(Predicate predicate, Pageable pageable) {
        JPAQuery<Doc> query = new JPAQuery<>(em)
                .select(qDoc)
                .from(qDoc)
                .leftJoin(qDocFileUpload).on(qDoc.id.eq(qDocFileUpload.doc.id))
                .leftJoin(qDocDownload).on(qDocFileUpload.id.eq(qDocDownload.docFileUpload.id))
                .where(predicate)
                .orderBy(qDocDownload.id.countDistinct().desc());
        query = groupByDoc(query);
        query = applyPaginatorPageOnly(query, pageable);

        Long totalElements = getDocTotalElements(predicate);

        return new PageImpl<>(query.fetch(), pageable, totalElements);
    }

    @Override
    public Page<Doc> quickSearch(Pageable pageable, String searchTerm) {
        SearchResult<Doc> searchResult = getDocSearchResult(
                searchTerm,
                null,
                null,
                null,
                null,
                DocStateType.APPROVED,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                null,
                null,
                DocBusinessState.PUBLIC,
                null,
        true
        );

        return this.searchResultToPage(searchResult, pageable.getPageNumber(), pageable.getPageSize());
    }

    @Override
    public void indexDoc(Long docID) {
        Doc doc = em.getReference(Doc.class, docID);
        indexDoc(doc);
    }

    @Override
    public void indexDoc(Doc doc) {
        SearchIndexingPlan indexingPlan = searchSession.indexingPlan();
        indexingPlan.addOrUpdate(doc);
        em.flush();
        indexingPlan.execute();
    }

    @Override
    public void removeIndexDoc(Long docID) {
        Doc doc = em.getReference(Doc.class, docID);
        removeIndexDoc(doc);
    }

    @Override
    public void removeIndexDoc(Doc doc) {
        SearchIndexingPlan indexingPlan = searchSession.indexingPlan();
        indexingPlan.delete(doc);
        em.flush();
        indexingPlan.execute();
    }

    private SearchResult<Doc> getDocSearchResult (String searchTerm,
                                                  String tagContent,
                                                  Long categoryID,
                                                  Long subjectID,
                                                  UUID authorID,
                                                  DocStateType docState,
                                                  Integer page,
                                                  Integer pageSize,
                                                  SortOrder sortByPublishDtm,
                                                  SortOrder sortByCreatedDtm,
                                                  DocBusinessState docBusinessState,
                                                  Authentication authentication,
                                                  boolean ignoreAuthorization) {

        SearchScope<Doc> scope = getSearchScope();

        //Filter search result based on user reading permission.
        SearchPredicate authorizationFilteringPredicate = (ignoreAuthorization) ? (null) : (DocHibernateSearchPredicateGenerator.getSearchPredicateOnAuthentication(authentication, scope)) ;

        //Generate predicate based on business state of doc.
        SearchPredicate businessStateFilteringPredicate = DocHibernateSearchPredicateGenerator.getSearchPredicateOnBusinessState(docBusinessState, scope);

        SearchResult<Doc> searchResult = searchSession.search(Doc.class)
                .where(f -> f.bool(b -> {
                    b.filter(f.matchAll());
                    if (!ignoreAuthorization) {
                        b.must(authorizationFilteringPredicate);
                    }
                    if (businessStateFilteringPredicate != null) {
                        b.must(businessStateFilteringPredicate);
                    }
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
                                .matching(em.getReference(Subject.class, subjectID))
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
                    if (tagContent != null) {
                        b.filter(f.match()
                                .field("tags_eb.content")
                                .matching(tagContent));
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

    private SearchScope<Doc> getSearchScope () { return searchSession.scope(Doc.class); }

    @PreDestroy
    public void cleanUp () throws IOException {
        luceneIndexReader.close();
    }

    private List<Doc> tupleToDocConverter (List<Tuple> tupleResult) {
//        List<Doc> entityList = new ArrayList<>();
//
//        for (Tuple row : tupleResult) {
//            Doc entity = new Doc();
//
//        }
        return null;
    }

    private long getDocTotalElements(Predicate predicate) {
        JPAQuery<Long> queryCount = new JPAQuery<>(em)
                .select(qDoc.id)
                .from(qDoc)
                .where(predicate);
        return queryCount.fetchCount();
    }

    //Workaround for buggy groupBy implementation of QueryDSL.
    private JPAQuery<Doc> groupByDoc (JPAQuery<Doc> query) {
        return query.groupBy(qDoc.id, qDoc.author.id, qDoc.lastEditedUser.id, qDoc.category.id, qDoc.subject.id, qDoc.description, qDoc.imageURL, qDoc.publishDtm, qDoc.submitDtm, qDoc.lastUpdatedDtm, qDoc.title, qDoc.docState, qDoc.deletedDtm, qDoc.version);
    }

    private JPAQuery<Doc> applyPaginatorPageOnly (JPAQuery<Doc> query, Pageable pageable) {
        return query.offset(pageable.getOffset()).limit(pageable.getPageSize());
    }

    private Page<Doc> searchResultToPage (SearchResult<Doc> docSearchResult, int page, int pageSize) {
        long resultCount = docSearchResult.total().hitCountLowerBound();
        int totalPages = (int)Math.ceil((double)resultCount/pageSize);
        List<Doc> docList = docSearchResult.hits();
        return new PageImpl<>(docList, PageRequest.of(page, pageSize), resultCount);
    }
}
