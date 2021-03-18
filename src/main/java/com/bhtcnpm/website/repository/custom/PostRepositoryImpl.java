package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.Post.*;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.PostEntities.QPost;
import com.bhtcnpm.website.model.entity.enumeration.PostState.PostStateType;
import com.bhtcnpm.website.repository.PostRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import javax.naming.directory.SearchResult;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

public class PostRepositoryImpl implements PostRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

    private final EntityPath<Post> path;
    private final PathBuilder<Post> builder;

    private final SearchSession searchSession;

    private final QPost qPost = QPost.post;

    private final Querydsl querydsl;

    public PostRepositoryImpl (EntityManager em) {
        this.em = em;
        this.path = SimpleEntityPathResolver.INSTANCE.createPath(Post.class);
        this.builder = new PathBuilder<Post>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(em, builder);
        this.searchSession = Search.session(em);
    }

    @Override
    public PostSummaryListDTO searchBySearchTerm(Predicate predicate, Pageable pageable, String searchTerm) {

        SearchResult<Post> result = searchSession.search(Post.class)
                .where(f -> f.phrase()
                        .fields("title", "summary", "contentPlainText")
                        .matching(searchTerm))
                .fetch(pageable);

        JPAQuery query = new JPAQuery<Post>(em)
                .select(Projections.constructor(PostSummaryDTO.class, qPost.id, qPost.author.id, qPost.author.name, qPost.author.avatarURL, qPost.category.id, qPost.category.name, qPost.imageURL, qPost.publishDtm, qPost.readingTime, qPost.summary, qPost.title))
                .from(qPost)
                .where(qPost.title.contains(searchTerm), predicate);

        JPQLQuery finalQuery = querydsl.applyPagination(pageable, query);

        QueryResults queryResults = finalQuery.fetchResults();

        List<PostSummaryDTO> listSummaryDTOs = queryResults.getResults();

        Long resultCount = finalQuery.fetchResults().getTotal();

        Integer totalPages = (int)Math.ceil((double)resultCount / pageable.getPageSize());

        PostSummaryListDTO result = new PostSummaryListDTO(listSummaryDTOs, totalPages, resultCount);

        return result;
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
