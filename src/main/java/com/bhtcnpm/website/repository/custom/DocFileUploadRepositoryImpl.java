package com.bhtcnpm.website.repository.custom;

import com.bhtcnpm.website.model.entity.DocEntities.DocFileUpload;
import com.bhtcnpm.website.model.entity.DocEntities.QDocFileUpload;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

@Component
public class DocFileUploadRepositoryImpl implements DocFileUploadRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

    private final QDocFileUpload qDocFileUpload = QDocFileUpload.docFileUpload;

    private final JPAQueryFactory queryFactory;

    public DocFileUploadRepositoryImpl (EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }
}
