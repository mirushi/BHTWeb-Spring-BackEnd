package com.bhtcnpm.website.security.predicate.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.QExerciseAttempt;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.UUID;

public class ExerciseAttemptPredicate {
    private static QExerciseAttempt qExerciseAttempt;

    public static BooleanExpression attemptOfUser (UUID userID) { return qExerciseAttempt.user.id.eq(userID); }
}
