package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.Post.HighlightPostRequestDTO;
import com.bhtcnpm.website.model.entity.PostEntities.HighlightPost;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.PostEntities.QHighlightPost;
import com.bhtcnpm.website.model.entity.PostEntities.QPost;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.time.LocalDateTime;

@Component
public class HighlightPostRepositoryImpl implements HighlightPostRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

    private final QHighlightPost qHighlightPost = QHighlightPost.highlightPost;

    private final JPAQueryFactory queryFactory;

    public HighlightPostRepositoryImpl (final EntityManager entityManager) {
        this.em = entityManager;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void createHighlightPost(Post post, UserWebsite user) {
        //1. Created post rank is always 1.
        //So we add 1 to all current highlight post rank.
        queryFactory
                .update(qHighlightPost)
                .set(qHighlightPost.rank, qHighlightPost.rank.add(1))
                .execute();

        //2. Add a highlight post to DB.
        HighlightPost highlightPost = HighlightPost.builder()
                .post(post)
                .rank(1)
                .highlightDtm(LocalDateTime.now())
                .highlightedBy(user)
                .build();

        em.persist(highlightPost);
    }

    @Override
    public void deleteHighlightPost(Long id) {
        JPQLQuery query = JPAExpressions.select(qHighlightPost.rank)
                .from(qHighlightPost)
                .where(qHighlightPost.id.eq(id));

        //1. Minus 1 to all rank higher than it.
        queryFactory.update(qHighlightPost)
                .set(qHighlightPost.rank, qHighlightPost.rank.subtract(1))
                .where(qHighlightPost.rank.gt(query)).execute();

        //2. Delete the highlight.
        queryFactory.delete(qHighlightPost)
                .where(qHighlightPost.id.eq(id)).execute();
    }

    @Override
    public void stickToTop(Long id) {
        JPQLQuery query = JPAExpressions.select(qHighlightPost.rank)
                .from(qHighlightPost)
                .where(qHighlightPost.id.eq(id));

        //1. Add 1 to all rank that is less than current rank.
        queryFactory.update(qHighlightPost)
                .set(qHighlightPost.rank, qHighlightPost.rank.add(1))
                .where(qHighlightPost.rank.lt(query)).execute();

        //2. Set current rank to 1.
        queryFactory.update(qHighlightPost)
                .set(qHighlightPost.rank, 1)
                .where(qHighlightPost.id.eq(id)).execute();
    }
}
