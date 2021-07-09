package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.model.dto.ExerciseQuestion.ExerciseQuestionResultDTO;
import com.bhtcnpm.website.model.dto.ExerciseQuestion.ExerciseQuestionSubmitDTO;
import com.bhtcnpm.website.model.dto.ExerciseQuestion.ExerciseQuestionWithAnswersDTO;
import com.bhtcnpm.website.model.dto.ExerciseQuestion.mapper.ExerciseQuestionMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseAnswer;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseAttempt;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseQuestion;
import com.bhtcnpm.website.repository.Exercise.ExerciseAttemptRepository;
import com.bhtcnpm.website.repository.Exercise.ExerciseQuestionRepository;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Exercise.ExerciseQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseQuestionServiceImpl implements ExerciseQuestionService {

    private final ExerciseQuestionRepository exerciseQuestionRepository;
    private final ExerciseQuestionMapper exerciseQuestionMapper;
    private final ExerciseAttemptRepository exerciseAttemptRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserWebsiteRepository userWebsiteRepository;

    @Override
    public List<ExerciseQuestionWithAnswersDTO> getExerciseQuestionWithAnswers(Long exerciseID) {
        List<ExerciseQuestion> exerciseQuestions = exerciseQuestionRepository.findAllByExerciseIdOrderByRankAsc(exerciseID);

        return exerciseQuestionMapper.exerciseQuestionListToExerciseQuestionWithAnswersDTOList(exerciseQuestions);
    }

    @Override
    public List<ExerciseQuestionResultDTO> submitAttemptAndGetResult(Long exerciseID, List<ExerciseQuestionSubmitDTO> submitDTOs, Authentication authentication) {
        Map<Long, ExerciseQuestionSubmitDTO> idExerciseQuestionSubmitMap = new HashMap<>(submitDTOs.size());
        for (ExerciseQuestionSubmitDTO submitDTO : submitDTOs) {
            idExerciseQuestionSubmitMap.put(submitDTO.getId(), submitDTO);
        }

        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAllByExerciseId(exerciseID);

        List<ExerciseQuestionResultDTO> resultList = new ArrayList<>();
        int correctAnsweredQuestion = 0;

        for (ExerciseQuestion exerciseQuestion : exerciseQuestionList) {
            if (!exerciseQuestion.getExercise().getId().equals(exerciseID)) {
                throw new IllegalArgumentException("ExerciseID does not match with submitted QuestionID");
            }

            ExerciseQuestionSubmitDTO submitDTO = idExerciseQuestionSubmitMap.get(exerciseQuestion.getId());
            if (submitDTO == null || submitDTO.getId() == null || submitDTO.getAnswersSelected() == null) {
                throw new IllegalArgumentException("Exercise Question submit is invalid.");
            }

            ExerciseQuestionResultDTO dto = new ExerciseQuestionResultDTO();
            dto.setId(exerciseQuestion.getId());

            //Bước 1: Lấy ra những câu trả lời user đã chọn và xác định những câu trả lời đúng.
            List<Long> answerSelected = submitDTO.getAnswersSelected();
            List<Long> correctAnswers = exerciseQuestion.getCorrectAnswers().stream().map(ExerciseAnswer::getId).collect(Collectors.toList());

            //Bước 2: Xác định user đã trả lời đúng chưa.
            boolean isCorrect = false;
            if (answerSelected.size() == correctAnswers.size() && answerSelected.containsAll(correctAnswers)) {
                isCorrect = true;
                ++correctAnsweredQuestion;
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

        //Xử lý lưu lại attempt của user nếu user đã đăng nhập.
        UUID userID = SecurityUtils.getUserID(authentication);
        if (userID != null) {
            ExerciseAttempt exerciseAttempt = new ExerciseAttempt();
            exerciseAttempt.setExercise(exerciseRepository.getOne(exerciseID));
            exerciseAttempt.setAttemptDtm(LocalDateTime.now());
            exerciseAttempt.setCorrectAnsweredQuestions(correctAnsweredQuestion);
            exerciseAttempt.setUser(userWebsiteRepository.getOne(userID));
            exerciseAttempt.setVersion((short)0);

            exerciseAttemptRepository.save(exerciseAttempt);
        }

        return resultList;
    }
}
