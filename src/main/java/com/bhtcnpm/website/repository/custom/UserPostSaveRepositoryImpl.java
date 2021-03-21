package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.Post.PostDetailsWithStateDTO;
import com.bhtcnpm.website.model.dto.Post.PostDetailsWithStateListDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryListDTO;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.PostEntities.QPost;
import com.bhtcnpm.website.model.entity.PostEntities.QUserPostSave;
import com.bhtcnpm.website.model.entity.PostEntities.UserPostSave;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class UserPostSaveRepositoryImpl implements UserPostSaveRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    private final EntityPath<Post> path;
    private final PathBuilder<Post> builder;

    private final QUserPostSave qUserPostSave = QUserPostSave.userPostSave;

    private final QPost qPost = QPost.post;

    private final Querydsl querydsl;

    public UserPostSaveRepositoryImpl (EntityManager entityManager) {
        this.em = entityManager;

        this.path = SimpleEntityPathResolver.INSTANCE.createPath(Post.class);
        this.builder = new PathBuilder<Post>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(em, builder);
    }

    @Override
    public PostSummaryListDTO findByUserPostSaveIdUserId(Long userID, Pageable pageable) {

        JPAQuery query = new JPAQuery<UserPostSave>(em)
                .select(Projections.constructor(PostSummaryDTO.class,
                        qPost.id,
                        qPost.author.id,
                        qPost.author.name,
                        qPost.author.avatarURL,
                        qPost.category.id,
                        qPost.category.name,
                        qPost.imageURL,
                        qPost.publishDtm,
                        qPost.readingTime,
                        qPost.summary,
                        qPost.title))
                .from(qUserPostSave)
                .innerJoin(qPost)
                .on(qUserPostSave.userPostSaveId.post.id.eq(qPost.id))
                .where(qPost.id.eq(userID));

        JPQLQuery finalQuery = querydsl.applyPagination(pageable, query);

        QueryResults queryResults = finalQuery.fetchResults();

        List<PostSummaryDTO> listDetailDTOs = queryResults.getResults();

        Long resultCount = finalQuery.fetchResults().getTotal();

        Integer totalPages = (int)Math.ceil((double)resultCount / pageable.getPageSize());

        PostSummaryListDTO result = new PostSummaryListDTO(listDetailDTOs, totalPages, resultCount);

        return result;
    }
}
