package com.bhtcnpm.website.repository.Subject;

import com.bhtcnpm.website.model.entity.SubjectEntities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SubjectRepository extends JpaRepository<Subject, Long>, QuerydslPredicateExecutor<Subject> {
}
