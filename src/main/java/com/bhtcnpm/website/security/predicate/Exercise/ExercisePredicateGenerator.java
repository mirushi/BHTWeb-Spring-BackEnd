package com.bhtcnpm.website.security.predicate.Exercise;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.UUID;

public class ExercisePredicateGenerator {
    public static BooleanExpression getBooleanExpressionExerciseUserAttempt (UUID userID) {
        return ExerciseAttemptPredicate.attemptOfUser(userID);
    }
}
