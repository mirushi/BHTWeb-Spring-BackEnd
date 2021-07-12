package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.constant.business.GenericBusinessConstant;
import com.bhtcnpm.website.constant.business.Post.PostBusinessConstant;
import com.bhtcnpm.website.constant.domain.Post.PostBusinessState;
import com.bhtcnpm.website.model.dto.Post.*;
import com.bhtcnpm.website.model.entity.PostEntities.*;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.search.lucene.LuceneIndexUtils;
import com.bhtcnpm.website.security.predicate.Post.PostHibernateSearchPredicateGenerator;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.Query;
import org.hibernate.search.backend.lucene.LuceneExtension;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.hibernate.search.engine.search.sort.dsl.SortThenStep;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.scope.SearchScope;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.orm.work.SearchIndexingPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.*;

@Component
public class PostRepositoryImpl implements PostRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

    private final EntityPath<Post> path;
    private final PathBuilder<Post> builder;

    private final SearchSession searchSession;

    private final QPost qPost = QPost.post;

    private final QPostView qPostView = QPostView.postView;

    private final QUserPostLike qUserPostLike = QUserPostLike.userPostLike;

    private final Querydsl querydsl;

    private PostMapper postMapper = PostMapper.INSTANCE;

    private final PostSuggestionMapper postSuggestionMapper = PostSuggestionMapper.INSTANCE;

    private final PostQuickSearchResultMapper postQuickSearchResultMapper = PostQuickSearchResultMapper.INSTANCE;

    private final IndexReader luceneIndexReader;

    public PostRepositoryImpl (EntityManager em) throws IOException {
        this.em = em;

        this.luceneIndexReader = LuceneIndexUtils.getReader("Post");
        this.path = SimpleEntityPathResolver.INSTANCE.createPath(Post.class);
        this.builder = new PathBuilder<Post>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(em, builder);
        this.searchSession = Search.session(em);
    }

    private SearchResult<Post> getPostSearchResult (String sortByPublishDtm,
                                                    Long postCategoryID,
                                                    Integer page,
                                                    Integer pageSize,
                                                    String searchTerm,
                                                    String tagContent,
                                                    PostStateType postStateType,
                                                    PostBusinessState postBusinessState,
                                                    Authentication authentication) {
        SearchScope<Post> scope = getSearchScope();

        //Build dynamic sorting condition based on user input.
        //Intermediate step.
        SortThenStep sortThenStep = null;

        //Final step.
        SortFinalStep sortFinalStep;

        //Avoid null pointer exception - inverse equals caller and callee.
        if (GenericBusinessConstant.SORT_ASC.equalsIgnoreCase(sortByPublishDtm)) {
            sortThenStep = scope.sort().field("publishDtm").asc();
        } else if (GenericBusinessConstant.SORT_DESC.equalsIgnoreCase(sortByPublishDtm)) {
            sortThenStep = scope.sort().field("publishDtm").desc();
        }

        //Sort then step is null. Which means user don't want to sort by publish datetime. Only by relevance.
        if (sortThenStep == null) {
            sortFinalStep = scope.sort().score();
        } else {
            sortFinalStep = sortThenStep.then().score();
        }

        //Filter search result based on user reading permission.
        SearchPredicate authorizationFilteringPredicate = PostHibernateSearchPredicateGenerator.getSearchPredicateOnAuthentication(authentication, scope);

        //Generate predicate based on business state of post.
        SearchPredicate businessStateFilteringPredicate = PostHibernateSearchPredicateGenerator.getSearchPredicateOnBusinessState(postBusinessState, scope);

        SearchResult<Post> searchResult = searchSession.search(Post.class)
                .where(f -> f.bool(b -> {
                    b.filter(f.matchAll());
                    b.must(authorizationFilteringPredicate);
                    if (businessStateFilteringPredicate != null) {
                        b.must(businessStateFilteringPredicate);
                    }
                    if (StringUtils.isNotEmpty(searchTerm)) {
                        b.must(f.match()
                                .field("title").boost(PostBusinessConstant.SEARCH_TITLE_BOOST)
                                .field("summary").boost(PostBusinessConstant.SEARCH_SUMMARY_BOOST)
                                .field("contentPlainText").boost(PostBusinessConstant.SEARCH_CONTENT_BOOST)
                                .matching(searchTerm));
                    }
                    if (postCategoryID != null) {
                        b.filter(f.match()
                                .field("categoryID")
                                .matching(em.getReference(PostCategory.class, postCategoryID)));
                    }
                    if (postStateType != null) {
                        b.filter(f.match()
                                .field("postState")
                                .matching(postStateType));
                    }
                    if (tagContent != null) {
                        b.filter(f.match()
                                .field("tags_eb.content")
                                .matching(tagContent));
                    }
                }))
                .sort(sortFinalStep.toSort())
                .fetch(page * pageSize, pageSize);

        return searchResult;
    }

    @Override
    public PostSummaryListDTO searchBySearchTerm(String sortByPublishDtm,
                                                 Long postCategoryID,
                                                 Integer page,
                                                 Integer pageSize,
                                                 String searchTerm,
                                                 String tagContent,
                                                 Authentication authentication) {

        SearchResult<Post> searchResult = getPostSearchResult(sortByPublishDtm,
                postCategoryID,
                page,
                pageSize,
                searchTerm,
                tagContent,
                null,
                PostBusinessState.PUBLIC,
                authentication);

        Long resultCount = searchResult.total().hitCountLowerBound();

        Integer totalPages = (int)Math.ceil((double)resultCount / pageSize);

        List<PostSummaryDTO> postSummaryListDTOS = postMapper.postListToPostSummaryDTOs(searchResult.hits());

        PostSummaryListDTO finalResult = new PostSummaryListDTO(postSummaryListDTOS, totalPages, resultCount);

        return finalResult;
    }

    @Override
    public PostSummaryWithStateListDTO getManagementPost(String sortByPublishDtm,
                                                         Long postCategoryID,
                                                         Integer page,
                                                         Integer pageSize,
                                                         String searchTerm,
                                                         PostStateType postStateType,
                                                         Authentication authentication) {
        SearchResult<Post> searchResult = getPostSearchResult(sortByPublishDtm,
                postCategoryID,
                page,
                pageSize,
                searchTerm,
                null,
                postStateType,
                null,
                authentication);

        Long resultCount = searchResult.total().hitCountLowerBound();

        Integer totalPages = (int)Math.ceil((double)resultCount / pageSize);

        List<PostSummaryWithStateDTO> postSummaryListDTOS = postMapper.postListToPostSummaryWithStateDTOList(searchResult.hits());

        PostSummaryWithStateListDTO finalResult = new PostSummaryWithStateListDTO(postSummaryListDTOS, totalPages, resultCount);

        return finalResult;
    }

    @Override
    public List<PostSuggestionDTO> searchRelatedPost (UUID authorID, Long categoryID, Long currentPostID, String title, String summary, String contentPlainText, int page , int pageSize,
                                                               PostBusinessState postBusinessState, Authentication authentication) throws IOException {
        //TODO: Until Hibernate Search re-implement support for moreLikeThis query, we'll use native Lucene query for relevance matching.
        MoreLikeThis mlt = new MoreLikeThis(luceneIndexReader);
        mlt.setFieldNames(new String[]{"title","summary", "contentPlainText"});
        mlt.setMinTermFreq(0);
        mlt.setMinDocFreq(0);
        mlt.setAnalyzer(LuceneIndexUtils.getStandardAnalyzer());

        Map<String, Collection<Object>> filteredDocument = new HashMap<>();
        filteredDocument.put("title", Collections.singletonList(title));
        filteredDocument.put("summary", Collections.singletonList(summary));
        filteredDocument.put("contentPlainText", Collections.singletonList(contentPlainText));

        Query query = mlt.like(filteredDocument);

        SearchScope<Post> scope = getSearchScope();

        SearchPredicate postBusinessStatePredicate = PostHibernateSearchPredicateGenerator.getSearchPredicateOnBusinessState(postBusinessState, scope);
        SearchPredicate authorizationPredicate = PostHibernateSearchPredicateGenerator.getSearchPredicateOnAuthentication(authentication, scope);

        SearchResult<Post> searchResults = searchSession.search(Post.class)
                .extension(LuceneExtension.get())
                .where(f -> f.bool(b -> {
                            b.must(f.fromLuceneQuery(query));
                            b.must(postBusinessStatePredicate);
                            b.must(authorizationPredicate);
                            if (currentPostID != null) {
                                b.mustNot(f.match()
                                        .field("id")
                                        .matching(currentPostID));
                            }
                            if (authorID != null) {
                                b.must(f.match()
                                        .field("authorID")
                                        .matching(em.getReference(UserWebsite.class, authorID)));
                            }
                            if (categoryID != null) {
                                b.must(f.match()
                                        .field("categoryID")
                                        .matching(em.getReference(PostCategory.class, categoryID)));
                            }

                        })
                ).fetch(page * pageSize, pageSize);

        List<Post> hits = searchResults.hits();

        return postSuggestionMapper.postListToPostSuggestionListDTO(hits);
    }

    @Override
    public List<PostSuggestionDTO> searchRelatedPost(UUID authorID, Long categoryID, Post entity, int page , int pageSize,
                                                     PostBusinessState postBusinessState, Authentication authentication) throws IOException {
        return searchRelatedPost(authorID, categoryID, entity.getId() ,entity.getTitle(), entity.getSummary(), entity.getContentPlainText(),
                page, pageSize, postBusinessState, authentication);
    }

    @Override
    public PostSummaryWithStateListDTO searchBySearchTermWithState(Predicate predicate, Pageable pageable) {

        JPAQuery query = new JPAQuery<Post>(em)
                .select(Projections.constructor(PostSummaryWithStateDTO.class, qPost.id, qPost.title, qPost.summary, qPost.imageURL, qPost.submitDtm, qPost.publishDtm, qPost.readingTime, qPost.author.id, qPost.author.displayName, qPost.author.avatarURL ,qPost.category.id, qPost.category.name, qPost.postState))
                .from(qPost)
                .where(predicate);

        JPQLQuery finalQuery = querydsl.applyPagination(pageable, query);

        QueryResults queryResults = finalQuery.fetchResults();

        List<PostSummaryWithStateDTO> listSummaryDTOs = queryResults.getResults();

        Long resultCount = finalQuery.fetchResults().getTotal();

        Integer totalPages = (int)Math.ceil((double)resultCount / pageable.getPageSize());

        PostSummaryWithStateListDTO result = new PostSummaryWithStateListDTO(listSummaryDTOs, totalPages, resultCount);

        return result;
    }

    @Override
    public PostSummaryWithStateAndFeedbackListDTO getPostSummaryStateFeedback(Predicate predicate, Pageable pageable) {
        JPAQuery query = new JPAQuery<Post>(em)
                .select(Projections.constructor(PostSummaryWithStateAndFeedbackDTO.class, qPost.id,qPost.title, qPost.summary, qPost.imageURL, qPost.submitDtm, qPost.publishDtm,qPost.readingTime , qPost.author.id, qPost.author.displayName, qPost.author.avatarURL ,qPost.category.id, qPost.category.name, qPost.adminFeedback ,qPost.postState))
                .from(qPost)
                .where(predicate);

        JPQLQuery finalQuery = querydsl.applyPagination(pageable, query);

        QueryResults queryResults = finalQuery.fetchResults();

        List<PostSummaryWithStateAndFeedbackDTO> listSummaryDTOs = queryResults.getResults();

        Long resultCount = finalQuery.fetchResults().getTotal();

        Integer totalPages = (int)Math.ceil((double)resultCount / pageable.getPageSize());

        PostSummaryWithStateAndFeedbackListDTO result = new PostSummaryWithStateAndFeedbackListDTO(listSummaryDTOs, totalPages, resultCount);

        return result;
    }

    @Override
    public PostDetailsWithStateListDTO getPostDetailsListWithStateFilter(Predicate predicate, Pageable pageable, PostStateType postStateType) {
        JPAQuery query = new JPAQuery<Post>(em)
                .select(Projections.constructor(PostDetailsWithStateDTO.class, qPost.id, qPost.title, qPost.imageURL, qPost.submitDtm, qPost.publishDtm, qPost.readingTime, qPost.content,  qPost.author.id, qPost.author.displayName, qPost.author.avatarURL ,qPost.category.id, qPost.category.name, qPost.postState))
                .from(qPost)
                .where(predicate)
                .where(qPost.postState.eq(postStateType));

        JPQLQuery finalQuery = querydsl.applyPagination(pageable, query);

        QueryResults queryResults = finalQuery.fetchResults();

        List<PostDetailsWithStateDTO> listDetailDTOs = queryResults.getResults();

        Long resultCount = finalQuery.fetchResults().getTotal();

        Integer totalPages = (int)Math.ceil((double)resultCount / pageable.getPageSize());

        PostDetailsWithStateListDTO result = new PostDetailsWithStateListDTO(listDetailDTOs, totalPages, resultCount);

        return result;
    }

    public List<PostQuickSearchResult> quickSearch (int page, int pageSize, String searchTerm) {
        SearchResult<Post> searchResult = searchSession.search(Post.class)
                .where(f -> f.bool(b -> {
                    b.filter(f.matchAll());
                    if (StringUtils.isNotEmpty(searchTerm)) {
                        b.must(f.match()
                                .field("title").boost(PostBusinessConstant.SEARCH_TITLE_BOOST)
                                .field("summary").boost(PostBusinessConstant.SEARCH_SUMMARY_BOOST)
                                .field("contentPlainText").boost(PostBusinessConstant.SEARCH_CONTENT_BOOST)
                                .matching(searchTerm));
                    }
                }))
                .fetch(page * pageSize, pageSize);

        //TODO: Consider optimize projection so that the result is projected instead of converting from mapper.

        List<Post> queryResult = searchResult.hits();

        List<PostQuickSearchResult> postQuickSearchResults = postQuickSearchResultMapper.postListToPostQuickSearchResultList(searchResult.hits());

        return postQuickSearchResults;
    }

    @Override
    public List<Post> findByCategoryNameOrderByPublishDtmDesc(Predicate predicate, Pageable pageable, String categoryName) {
        JPAQuery<Post> query = new JPAQuery<Post>(em)
                .select(qPost)
                .from(qPost)
                .where(predicate)
                .where(qPost.category.name.eq(categoryName))
                .orderBy(qPost.publishDtm.desc());

        JPQLQuery<Post> finalQuery = querydsl.applyPagination(pageable, query);

        return finalQuery.fetch();
    }

    @Override
    public Page<Post> getPostOrderByViewCountDESC(Predicate predicate, Pageable pageable) {
        JPAQuery<Post> query = new JPAQuery<>(em)
                .select(qPost)
                .from(qPost)
                .leftJoin(qPostView).on(qPost.id.eq(qPostView.post.id))
                .where(predicate)
                .orderBy(qPostView.id.countDistinct().desc());
        query = groupByPost(query);
        query = applyPaginatorPageOnly(query, pageable);

        Long totalElements = getPostTotalElements(predicate);

        return new PageImpl<>(query.fetch(), pageable, totalElements);
    }

    @Override
    public Page<Post> getPostOrderByLikeCountDESC(Predicate predicate, Pageable pageable) {
        JPAQuery<Post> query = new JPAQuery<>(em)
                .select(qPost)
                .from(qPost)
                .leftJoin(qUserPostLike).on(qUserPostLike.userPostLikeId.post.id.eq(qPost.id))
                .where(predicate)
                .orderBy(qUserPostLike.userPostLikeId.user.id.countDistinct().desc());
        query = groupByPost(query);
        query = applyPaginatorPageOnly(query, pageable);

        Long totalElements = getPostTotalElements(predicate);

        return new PageImpl<>(query.fetch(), pageable, totalElements);
    }

    private SearchScope<Post> getSearchScope () {return searchSession.scope(Post.class);}

    @Override
    public void indexPost(Long postID) {
        Post post = em.getReference(Post.class, postID);
        indexPost(post);
    }

    @Override
    public void indexPost(Post post) {
        SearchIndexingPlan indexingPlan = searchSession.indexingPlan();
        indexingPlan.addOrUpdate(post);
        em.flush();
        indexingPlan.execute();
    }

    @Override
    public void removeIndexPost(Long postID) {
        Post post = em.getReference(Post.class, postID);
        removeIndexPost(post);
    }

    @Override
    public void removeIndexPost(Post post) {
        SearchIndexingPlan indexingPlan = searchSession.indexingPlan();
        indexingPlan.delete(post);
        em.flush();
        indexingPlan.execute();
    }

    @PreDestroy
    public void cleanUp() throws IOException {
        luceneIndexReader.close();
    }

    private long getPostTotalElements (Predicate predicate) {
        JPAQuery<Long> queryCount = new JPAQuery<>(em)
                .select(qPost.id)
                .from(qPost)
                .where(predicate);
        return queryCount.fetchCount();
    }

    private JPAQuery<Post> applyPaginatorPageOnly(JPAQuery<Post> query, Pageable pageable) {
        return query.offset(pageable.getOffset()).limit(pageable.getPageSize());
    }

    private JPAQuery<Post> groupByPost (JPAQuery<Post> query) {
        return query.groupBy(qPost.id, qPost.title, qPost.summary, qPost.imageURL, qPost.submitDtm, qPost.publishDtm, qPost.lastUpdatedDtm, qPost.lastUpdatedBy.id, qPost.readingTime, qPost.content, qPost.contentPlainText, qPost.adminFeedback, qPost.author.id, qPost.category.id, qPost.postState, qPost.deletedDate, qPost.deletedBy.id);
    }
}
