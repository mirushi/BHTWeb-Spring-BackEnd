package com.bhtcnpm.website.repository.Subject;

import com.bhtcnpm.website.model.entity.SubjectEntities.SubjectFaculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface SubjectFacultyRepository extends JpaRepository<SubjectFaculty, Long>, QuerydslPredicateExecutor<SubjectFaculty> {
}
