package com.bhtcnpm.website.repository;

import com.bhtcnpm.website.model.entity.UserDocReaction;
import com.bhtcnpm.website.repository.custom.UserDocReactionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Set;

public interface UserDocReactionRepository extends JpaRepository<UserDocReaction, Long>, QuerydslPredicateExecutor<UserDocReaction>, UserDocReactionRepositoryCustom {

    Set<UserDocReaction> getUserDocReactionsByUserDocReactionId_UserIdAndUserDocReactionId_DocId_IdIn (Long userId, Set<Long> docIds);

}
