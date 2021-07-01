package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.entity.PostEntities.QUserPostReport;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class UserPostReportRepositoryImpl implements UserPostReportRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    private final QUserPostReport qUserPostReport = QUserPostReport.userPostReport;

    private final JPAQueryFactory queryFactory;

    public UserPostReportRepositoryImpl (EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
}
