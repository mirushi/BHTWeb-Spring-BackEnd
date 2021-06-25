package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.model.dto.Exercise.*;
import com.bhtcnpm.website.model.dto.Exercise.mapper.ExerciseMapper;
import com.bhtcnpm.website.model.dto.Exercise.mapper.ExerciseTopicMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseTopic;
import com.bhtcnpm.website.repository.Exercise.ExerciseTopicRepository;
import com.bhtcnpm.website.security.predicate.Exercise.ExercisePredicateGenerator;
import com.bhtcnpm.website.service.Exercise.ExerciseService;
import com.bhtcnpm.website.service.Exercise.ExerciseTopicService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseTopicServiceImpl implements ExerciseTopicService {

    private final ExerciseTopicRepository exerciseTopicRepository;

    private final ExerciseService exerciseService;

    private final ExerciseTopicMapper exerciseTopicMapper;

    private final ExerciseMapper exerciseMapper;

    @Override
    public List<ExerciseTopicDTO> getAllExerciseTopicsBySubject(Predicate predicate) {
        Iterable<ExerciseTopic> exerciseTopicList = exerciseTopicRepository.findAll(predicate);

        return exerciseTopicMapper.exerciseTopicIterableToExerciseTopicDTOList(exerciseTopicList);
    }

    @Override
    public List<ExerciseTopicWithExerciseListDTO> getAllExerciseTopicsWithExerciseList(Long subjectID, Authentication authentication) {
        BooleanExpression fetchBySubjectID = ExercisePredicateGenerator.getBooleanExpressionAllExerciseOfSubject(subjectID);
        //Đầu tiên là get ra hết tất cả những bài tập thuộc môn học này.
        List<ExerciseSummaryWithTopicDTO> exerciseSummaryWithTopicDTOList = exerciseService.getExerciseWithTopic(fetchBySubjectID, authentication);
        //Lấy ra hết những topic thuộc môn học này.
        List<ExerciseTopic> exerciseTopicList = exerciseTopicRepository.findAllBySubjectIdOrderByRankAsc(subjectID);
        //Ghép tất cả những bài tập vào từng môn học thích hợp.
        //B1: Đẩy tất cả những môn học vào hashtable tương ứng.
        Map<Long, List<ExerciseSummaryDTO>> exerciseOfTopic = new HashMap<>(exerciseTopicList.size());

        for (ExerciseSummaryWithTopicDTO exercise : exerciseSummaryWithTopicDTOList) {
            Long topicID = exercise.getTopicID();
            List<ExerciseSummaryDTO> exerciseSummaryDTOList = exerciseOfTopic.get(topicID);

            if (exerciseSummaryDTOList == null) {
                exerciseSummaryDTOList = new ArrayList<>();
            }

            ExerciseSummaryDTO exerciseSummaryDTO = exerciseMapper.exerciseWithTopicToExerciseSummaryDTO(exercise);
            exerciseSummaryDTOList.add(exerciseSummaryDTO);

            exerciseOfTopic.put(topicID, exerciseSummaryDTOList);
        }

        //B2: Tạo ra các DTO mới chứa topic trong đó chứa những bài tập.
        List<ExerciseTopicWithExerciseListDTO> result = new ArrayList<>();
        for (ExerciseTopic topic : exerciseTopicList) {
            List<ExerciseSummaryDTO> exerciseSummaryDTOList = exerciseOfTopic.get(topic.getId());
            ExerciseTopicWithExerciseListDTO exerciseTopicWithExerciseListDTO = new ExerciseTopicWithExerciseListDTO();

            exerciseTopicWithExerciseListDTO.setId(topic.getId());
            exerciseTopicWithExerciseListDTO.setName(topic.getName());
            exerciseTopicWithExerciseListDTO.setExerciseSummaryDTOs(exerciseSummaryDTOList);

            result.add(exerciseTopicWithExerciseListDTO);
        }

        return result;
    }
}
