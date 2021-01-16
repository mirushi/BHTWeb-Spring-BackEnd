package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.dto.UserDocReaction.UserDocReactionStatsDTO;
import com.bhtcnpm.website.model.entity.QUserDocReaction;
import com.querydsl.collections.CollQueryFactory;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.QTuple;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import static com.querydsl.core.group.GroupBy.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class UserDocReactionRepositoryImpl implements UserDocReactionRepositoryCustom{

    @PersistenceContext
    private final EntityManager em;

    private static final QUserDocReaction userDocReaction = QUserDocReaction.userDocReaction;

//    @Override
//    public Map<List<?>, Long> getUserDocReactionStatsDTO (List<Long> docIDs) {
//        Map<List<?>, Long> queryResult = CollQueryFactory
//                .from(userDocReaction)
//                .where(userDocReaction.userDocReactionId.doc.id.in(docIDs))
//                .transform(groupBy(userDocReaction.userDocReactionId.doc.id, userDocReaction.docReactionType).as(userDocReaction.userDocReactionId.user.id.count()));
//        Map<Long, Long> queryResult = CollQueryFactory
//                .from(userDocReaction)
//                .where(userDocReaction.userDocReactionId.doc.id.in(docIDs))
//                .transform(groupBy(userDocReaction.userDocReactionId.doc.id).as(userDocReaction.userDocReactionId.user.id.count()));
//        return null;
//    }
}
