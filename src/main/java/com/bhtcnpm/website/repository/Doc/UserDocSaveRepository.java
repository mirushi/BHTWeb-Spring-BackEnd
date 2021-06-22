package com.bhtcnpm.website.repository.Doc;

import com.bhtcnpm.website.model.entity.DocEntities.UserDocSave;
import com.bhtcnpm.website.model.entity.DocEntities.UserDocSaveId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDocSaveRepository extends JpaRepository<UserDocSave, UserDocSaveId>, QuerydslPredicateExecutor<UserDocSave> {
}
