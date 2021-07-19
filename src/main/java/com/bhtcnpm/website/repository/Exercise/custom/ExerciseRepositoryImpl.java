package com.bhtcnpm.website.repository.Exercise.custom;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseSummaryWithTopicDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.QExercise;
import com.bhtcnpm.website.model.entity.ExerciseEntities.QExerciseAttempt;
import com.bhtcnpm.website.model.entity.ExerciseEntities.QExerciseQuestion;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Component
public class ExerciseRepositoryImpl implements ExerciseRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;

    private final QExercise qExercise = QExercise.exercise;

    private final QExerciseAttempt qExerciseAttempt = QExerciseAttempt.exerciseAttempt;

    private final QExerciseQuestion qExerciseQuestion = QExerciseQuestion.exerciseQuestion;

    public ExerciseRepositoryImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<ExerciseSummaryDTO> getExerciseSummaryWithUserAttempts(Predicate predicate, UUID userID) {
//        NumberExpression<Integer> userAttemption = new CaseBuilder()
//                .when(qExerciseAttempt.user.id.eq(userID)).then(1).otherwise(0);
//
//        BooleanExpression isUserAttempted = new CaseBuilder()
//                .when(userAttemption.sum().gt(0)).then(true).otherwise(false);
//
//        NumberExpression<Integer> userCorrectAnsweredQuestion
//
//        JPAQuery<ExerciseSummaryDTO> query = new JPAQuery<ExerciseSummaryDTO>(em)
//                .select(Projections.constructor(ExerciseSummaryDTO.class, qExercise.id, qExercise.title, qExercise.description, isUserAttempted,
//                        qExerciseAttempt.correctAnsweredQuestions.max(), qExerciseQuestion.id.countDistinct()))
//                .from(qExercise)
//                .leftJoin(qExerciseAttempt).on(qExercise.id.eq(qExerciseAttempt.exercise.id))
//                .leftJoin(qExerciseQuestion).on(qExercise.id.eq(qExerciseQuestion.exercise.id))
//                .where(predicate)
//                .groupBy(qExercise.id, qExercise.title, qExercise.description, qExercise.rank)
//                .orderBy(qExercise.rank.asc());
//
//        return query.fetch();

        return null;
    }

    @Override
    public List<ExerciseSummaryWithTopicDTO> getExerciseSummaryWithTopicAndUserAttempts(Predicate predicate, UUID userID) {
//        BooleanExpression isUserAttempted = new CaseBuilder()
//                .when(qExerciseAttempt.countDistinct().gt(0)).then(true).otherwise(false);
//
//        JPAQuery<ExerciseSummaryWithTopicDTO> query = new JPAQuery<ExerciseSummaryWithTopicDTO>(em)
//                .select(Projections.constructor(ExerciseSummaryWithTopicDTO.class, qExercise.id, qExercise.title, qExercise.description, isUserAttempted,
//                        qExerciseAttempt.correctAnsweredQuestions.max(),
//                        qExerciseQuestion.id.countDistinct(),
//                        qExercise.topic.id, qExercise.topic.name))
//                .from(qExercise).leftJoin(qExerciseAttempt).on(qExercise.id.eq(qExerciseAttempt.exercise.id))
//                .leftJoin(qExerciseQuestion).on(qExercise.id.eq(qExerciseQuestion.exercise.id))
//                .where(predicate)
//                .groupBy(qExercise.id, qExercise.title, qExercise.description, qExercise.topic.id, qExercise.topic.name)
//                .orderBy(qExercise.rank.asc());
//
//        return query.fetch();

        return null;
    }
}
