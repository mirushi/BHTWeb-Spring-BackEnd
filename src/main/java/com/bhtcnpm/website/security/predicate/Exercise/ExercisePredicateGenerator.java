package com.bhtcnpm.website.security.predicate.Exercise;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.UUID;

public class ExercisePredicateGenerator {
    public static BooleanExpression getBooleanExpressionExerciseUserAttempt (UUID userID) {
        return ExerciseAttemptPredicate.attemptOfUser(userID);
    }
    public static BooleanExpression getBooleanExpressionAllExerciseOfSubject (Long subjectID) {
        return ExercisePredicate.bySubjectID(subjectID);
    }
}
