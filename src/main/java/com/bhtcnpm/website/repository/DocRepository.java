package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.Doc;
import com.bhtcnpm.website.repository.custom.DocRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DocRepository extends JpaRepository<Doc, Long>, QuerydslPredicateExecutor<Doc>,DocRepositoryCustom {
}
