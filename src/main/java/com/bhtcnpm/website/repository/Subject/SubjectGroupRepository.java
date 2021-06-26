package com.bhtcnpm.website.repository.Subject;

import com.bhtcnpm.website.model.entity.SubjectEntities.SubjectGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SubjectGroupRepository extends JpaRepository<SubjectGroup, Long>, QuerydslPredicateExecutor<SubjectGroup> {
}
