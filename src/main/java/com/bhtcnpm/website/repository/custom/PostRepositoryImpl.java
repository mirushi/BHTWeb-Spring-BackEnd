package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.constant.business.GenericBusinessConstant;
import com.bhtcnpm.website.model.dto.Post.*;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.PostEntities.PostCategory;
import com.bhtcnpm.website.model.entity.PostEntities.QPost;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.engine.search.predicate.dsl.PhrasePredicateOptionsStep;
import org.hibernate.search.engine.search.predicate.dsl.PredicateFinalStep;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.engine.search.sort.SearchSort;
import org.hibernate.search.engine.search.sort.dsl.SortFinalStep;
import org.hibernate.search.engine.search.sort.dsl.SortThenStep;
import org.hibernate.search.engine.search.sort.spi.SearchSortBuilder;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.scope.SearchScope;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PostRepositoryImpl implements PostRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

    private final EntityPath<Post> path;
    private final PathBuilder<Post> builder;

    private final SearchSession searchSession;

    private final QPost qPost = QPost.post;

    private final Querydsl querydsl;

    private PostMapper postMapper = PostMapper.INSTANCE;

    public PostRepositoryImpl (EntityManager em) {
        this.em = em;
        this.path = SimpleEntityPathResolver.INSTANCE.createPath(Post.class);
        this.builder = new PathBuilder<Post>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(em, builder);
        this.searchSession = Search.session(em);
    }

    @Override
    public PostSummaryListDTO searchBySearchTerm(String sortByPublishDtm, Long postCategoryID ,Integer page, Integer pageSize ,String searchTerm) {
        SearchScope<Post> scope = searchSession.scope(Post.class);

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

        SearchResult<Post> searchResult = searchSession.search(Post.class)
                .where(f -> f.bool(b -> {
                    b.filter(f.matchAll());
                    if (StringUtils.isNotEmpty(searchTerm)) {
                        b.filter(f.match()
                                .field("title").boost(2.0f)
                                .field("summary").boost(1.5f)
                                .field("contentPlainText")
                                .matching(searchTerm));
                    }
                    if (postCategoryID != null) {
                        b.filter(f.match()
                                .field("categoryID")
                                .matching(em.getReference(PostCategory.class, postCategoryID)));
                    }
                }))
                .sort(sortFinalStep.toSort())
                .fetch(page * pageSize, pageSize);

        Long resultCount = searchResult.total().hitCountLowerBound();

        Integer totalPages = (int)Math.ceil((double)resultCount / pageSize);

        List<PostSummaryDTO> postSummaryListDTOS = postMapper.postListToPostSummaryDTOs(searchResult.hits());

        PostSummaryListDTO finalResult = new PostSummaryListDTO(postSummaryListDTOS, totalPages, resultCount);

        return finalResult;
    }

    @Override
    public PostSummaryWithStateListDTO searchBySearchTermWithState(Predicate predicate, Pageable pageable) {

        JPAQuery query = new JPAQuery<Post>(em)
                .select(Projections.constructor(PostSummaryWithStateDTO.class, qPost.id, qPost.author.id, qPost.author.name, qPost.author.avatarURL ,qPost.category.id, qPost.category.name, qPost.imageURL, qPost.publishDtm, qPost.readingTime, qPost.summary, qPost.title, qPost.postState))
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
                .select(Projections.constructor(PostSummaryWithStateAndFeedbackDTO.class, qPost.id, qPost.author.id, qPost.author.name, qPost.author.avatarURL ,qPost.category.id, qPost.category.name, qPost.imageURL, qPost.publishDtm, qPost.readingTime, qPost.summary, qPost.title, qPost.postState, qPost.adminFeedback))
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
                .select(Projections.constructor(PostDetailsWithStateDTO.class, qPost.id, qPost.author.id, qPost.author.name, qPost.author.avatarURL ,qPost.category.id, qPost.category.name, qPost.imageURL, qPost.publishDtm, qPost.readingTime, qPost.content, qPost.title, qPost.postState))
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

}
