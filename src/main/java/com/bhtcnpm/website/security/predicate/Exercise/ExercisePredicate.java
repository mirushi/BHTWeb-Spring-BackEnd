package com.bhtcnpm.website.security.predicate.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.QExercise;
import com.querydsl.core.types.dsl.BooleanExpression;

public class ExercisePredicate {
    private static QExercise qExercise = QExercise.exercise;

    public static BooleanExpression bySubjectID (Long subjectID) {
        return qExercise.topic.subject.id.eq(subjectID);
    }
}
