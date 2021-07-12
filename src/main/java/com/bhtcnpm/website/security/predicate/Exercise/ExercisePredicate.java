package com.bhtcnpm.website.security.predicate.Exercise;

import com.bhtcnpm.website.model.entity.ExerciseEntities.QExercise;
import com.bhtcnpm.website.model.entity.enumeration.ExerciseState.ExerciseStateType;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.time.LocalDateTime;
import java.util.UUID;

public class ExercisePredicate {
    private static QExercise qExercise = QExercise.exercise;

    public static BooleanExpression bySubjectID (Long subjectID) {
        return qExercise.topic.subject.id.eq(subjectID);
    }
    public static BooleanExpression approved() { return qExercise.exerciseState.eq(ExerciseStateType.APPROVED); }
    public static BooleanExpression notApproved () { return qExercise.exerciseState.ne(ExerciseStateType.APPROVED) ; }
    public static BooleanExpression deleted() { return qExercise.deletedDtm.isNotNull(); }
    public static BooleanExpression notDeleted() { return qExercise.deletedDtm.isNull(); }
    public static BooleanExpression userOwn(UUID userID) { return qExercise.author.id.eq(userID); }
    public static BooleanExpression exercisePublishDtmReached() { return qExercise.publishDtm.loe(LocalDateTime.now()); }
    public static BooleanExpression exercisePublishDtmNotReached() { return qExercise.publishDtm.gt(LocalDateTime.now()); }
    public static BooleanExpression exercisePublicBusinessState() {
        return approved().and(notDeleted()).and(exercisePublishDtmReached());
    }
    public static BooleanExpression exerciseUnlistedBusinessState() {
        return notDeleted().and(notApproved().or(exercisePublishDtmNotReached()));
    }
    public static BooleanExpression exerciseStateType(ExerciseStateType exerciseStateType) { return qExercise.exerciseState.eq(exerciseStateType); }
}
