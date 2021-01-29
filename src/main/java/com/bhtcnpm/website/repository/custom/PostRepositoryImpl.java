package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.Post.PostSummaryDTO;
import com.bhtcnpm.website.model.dto.Post.PostSummaryListDTO;
import com.bhtcnpm.website.model.entity.PostEntities.Post;
import com.bhtcnpm.website.model.entity.PostEntities.QPost;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

    private final QPost qPost = QPost.post;

    @Override
    public PostSummaryListDTO searchBySearchTerm(Predicate predicate, Pageable pageable, String searchTerm) {
//
//        String searchTerm2 = "kakaka";
//
//        List<PostSummaryDTO> listSummaryDTOs = new JPAQuery<Post>(em)
//                .select(Projections.constructor(PostSummaryDTO.class, qPost.id, qPost.author.id, qPost.author.name, qPost.category.id, qPost.category.name, qPost.imageURL, qPost.publishDtm, qPost.readingTime, qPost.summary, qPost.title))
//                .from(qPost)
//                .where(qPost.title.like(searchTerm), predicate)
//                .orderBy(qPost.title.eq(searchTerm2).desc(), qPost.title.length().asc())
//                .offset(pageable.getPageNumber() * pageable.getPageSize())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        Integer totalPages = 1;
//
//        PostSummaryListDTO result = new PostSummaryListDTO(listSummaryDTOs, totalPages);

        return null;
    }
}
