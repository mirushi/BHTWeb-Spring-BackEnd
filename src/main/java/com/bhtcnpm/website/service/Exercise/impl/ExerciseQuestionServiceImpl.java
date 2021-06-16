package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseQuestionDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseQuestionResultDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseQuestionSubmitDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseQuestionWithAnswersDTO;
import com.bhtcnpm.website.model.dto.Exercise.mapper.ExerciseQuestionMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseAnswer;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseAttempt;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseQuestion;
import com.bhtcnpm.website.repository.Exercise.ExerciseAttemptRepository;
import com.bhtcnpm.website.repository.Exercise.ExerciseQuestionRepository;
import com.bhtcnpm.website.service.Exercise.ExerciseQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseQuestionServiceImpl implements ExerciseQuestionService {

    private final ExerciseQuestionRepository exerciseQuestionRepository;
    private final ExerciseQuestionMapper exerciseQuestionMapper;
    private final ExerciseAttemptRepository exerciseAttemptRepository;

    @Override
    public List<ExerciseQuestionWithAnswersDTO> getExerciseQuestionWithAnswers(Long exerciseID) {
        List<ExerciseQuestion> exerciseQuestions = exerciseQuestionRepository.findAllByExerciseIdOrderByRankAsc(exerciseID);

        return exerciseQuestionMapper.exerciseQuestionListToExerciseQuestionWithAnswersDTOList(exerciseQuestions);
    }

    @Override
    public List<ExerciseQuestionResultDTO> submitAttemptAndGetResult(List<ExerciseQuestionSubmitDTO> submitDTOs, Authentication authentication) {
        Map<Long, ExerciseQuestionSubmitDTO> idExerciseQuestionSubmitMap = new HashMap<>(submitDTOs.size());
        for (ExerciseQuestionSubmitDTO submitDTO : submitDTOs) {
            idExerciseQuestionSubmitMap.put(submitDTO.getId(), submitDTO);
        }

        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAllByIdIn(idExerciseQuestionSubmitMap.keySet());

        List<ExerciseQuestionResultDTO> resultList = new ArrayList<>();
        for (ExerciseQuestion exerciseQuestion : exerciseQuestionList) {
            ExerciseQuestionResultDTO dto = new ExerciseQuestionResultDTO();
            dto.setId(exerciseQuestion.getId());

            ExerciseQuestionSubmitDTO submitDTO = idExerciseQuestionSubmitMap.get(exerciseQuestion.getId());

            //Bước 1: Lấy ra những câu trả lời user đã chọn và xác định những câu trả lời đúng.
            List<Long> answerSelected = submitDTO.getAnswersSelected();
            List<Long> correctAnswers = exerciseQuestion.getCorrectAnswers().stream().map(ExerciseAnswer::getId).collect(Collectors.toList());

            //Bước 2: Xác định user đã trả lời đúng chưa.
            boolean isCorrect = false;
            if (answerSelected.size() == correctAnswers.size() && answerSelected.containsAll(correctAnswers)) {
                isCorrect = true;
            }

            //Bước 3: Xác định user có trả lời câu hỏi này chưa.
            boolean isAnswered = answerSelected.size() > 0;

            //Bước 4: Set data và thêm dto vào danh sách kết quả trả về.
            dto.setIsCorrect(isCorrect);
            dto.setIsAnswered(isAnswered);
            dto.setAnswersSelected(answerSelected);
            dto.setCorrectAnswers(correctAnswers);
            dto.setExplanation(exerciseQuestion.getExplanation());

            resultList.add(dto);
        }

        return resultList;
    }
}
