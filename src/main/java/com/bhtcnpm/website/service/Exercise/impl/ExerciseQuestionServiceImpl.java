package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.model.dto.ExerciseQuestion.*;
import com.bhtcnpm.website.model.dto.ExerciseQuestion.mapper.ExerciseQuestionMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.*;
import com.bhtcnpm.website.repository.Exercise.ExerciseAttemptRepository;
import com.bhtcnpm.website.repository.Exercise.ExerciseQuestionDifficultyRepository;
import com.bhtcnpm.website.repository.Exercise.ExerciseQuestionRepository;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Exercise.ExerciseQuestionService;
import com.bhtcnpm.website.service.Exercise.ExerciseService;
import com.bhtcnpm.website.service.ExerciseSubjectUserScoreService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
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
    private final ExerciseQuestionDifficultyRepository exerciseQuestionDifficultyRepository;
    private final ExerciseSubjectUserScoreService exerciseSubjectUserScoreService;
    private final ExerciseQuestionMapper exerciseQuestionMapper;
    private final ExerciseAttemptRepository exerciseAttemptRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseService exerciseService;
    private final UserWebsiteRepository userWebsiteRepository;

    @Override
    public List<ExerciseQuestionWithAnswersPublicDTO> getPublicExerciseQuestionWithAnswers(Long exerciseID) {
        List<ExerciseQuestion> exerciseQuestions = exerciseQuestionRepository.findAllByExerciseIdOrderByRankAsc(exerciseID);

        return exerciseQuestionMapper.exerciseQuestionListToExerciseQuestionWithAnswersPublicDTOList(exerciseQuestions);
    }

    @Override
    public List<ExerciseQuestionWithAnswersDTO> getExerciseQuestionsWithAnswers(Long exerciseID) {
        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAllByExerciseIdOrderByRankAsc(exerciseID);

        return exerciseQuestionMapper.exerciseQuestionListToExerciseQuestionWithAnswersDTOList(exerciseQuestionList);
    }

    @Override
    public List<ExerciseQuestionResultDTO> submitAttemptAndGetResult(Long exerciseID, List<ExerciseQuestionSubmitDTO> submitDTOs, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(exerciseID);
        Validate.isTrue(exerciseOpt.isPresent(), String.format("Exercise with id = %s not found.", exerciseOpt));
        Exercise exerciseEntity = exerciseOpt.get();

        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAllByExerciseId(exerciseID);

        List<ExerciseQuestionDifficulty> difficultyTypeList = exerciseQuestionDifficultyRepository.findAll();
        Map<Integer, Integer> difficultyScore = new HashMap<>();
        for (ExerciseQuestionDifficulty difficulty : difficultyTypeList) {
            difficultyScore.put(difficulty.getId(), difficulty.getScore());
        }

        Map<Long, ExerciseQuestionSubmitDTO> idExerciseQuestionSubmitMap = new HashMap<>(submitDTOs.size());
        for (ExerciseQuestionSubmitDTO submitDTO : submitDTOs) {
            idExerciseQuestionSubmitMap.put(submitDTO.getId(), submitDTO);
        }

        List<ExerciseQuestionResultDTO> resultList = new ArrayList<>();
        int correctAnsweredQuestion = 0;
        int totalScore = 0;

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
                if (exerciseQuestion.getDifficultyType() != null) {
                    totalScore += difficultyScore.get(exerciseQuestion.getDifficultyType().getId());
                }
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
        if (userID != null) {
            //Lấy về điểm tổng tốt nhất của user trước đó.
            Integer userPreviousMaxScore = exerciseAttemptRepository.findMaxScoreAttemptByUserInExercise(userID, exerciseID);
            int scoreIncrease = 0;
            if (userPreviousMaxScore == null) {
                scoreIncrease = totalScore;
            } else if (totalScore > userPreviousMaxScore) {
                scoreIncrease = totalScore - userPreviousMaxScore;
            }
            if (scoreIncrease > 0) {
                exerciseSubjectUserScoreService.addScore(userID, exerciseEntity.getTopic().getSubject().getId(), scoreIncrease);
            }

            ExerciseAttempt exerciseAttempt = new ExerciseAttempt();
            exerciseAttempt.setExercise(exerciseRepository.getOne(exerciseID));
            exerciseAttempt.setAttemptDtm(LocalDateTime.now());
            exerciseAttempt.setCorrectAnsweredQuestions(correctAnsweredQuestion);
            exerciseAttempt.setTotalScore(totalScore);
            exerciseAttempt.setUser(userWebsiteRepository.getOne(userID));
            exerciseAttempt.setVersion((short)0);

            exerciseAttemptRepository.save(exerciseAttempt);

            //Cập nhập biến đếm.
            exerciseService.updateAttempts(exerciseID);
        }

        return resultList;
    }

    @Override
    public ExerciseQuestionDTO createQuestion(Long exerciseID, ExerciseQuestionRequestDTO requestDTO, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        ExerciseQuestion exerciseQuestion = exerciseQuestionMapper.exerciseQuestionRequestDTOToExerciseQuestion(requestDTO, exerciseID, userID);
        exerciseQuestion = exerciseQuestionRepository.save(exerciseQuestion);
        return exerciseQuestionMapper.exerciseQuestionToExerciseQuestionPublicDTO(exerciseQuestion);
    }

    @Override
    public List<ExerciseQuestionDTO> createMultipleQuestions(Long exerciseID, List<ExerciseQuestionRequestDTO> requestDTOList, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionMapper.exerciseQuestionRequestDTOListToExerciseQuestionList(requestDTOList, exerciseID, userID);

        exerciseQuestionList = exerciseQuestionRepository.saveAll(exerciseQuestionList);

        return exerciseQuestionMapper.exerciseQuestionListToExerciseQuestionPublicDTOList(exerciseQuestionList);
    }

    @Override
    public ExerciseQuestionWithAnswersDTO createQuestionWithAnswers(Long exerciseID, ExerciseQuestionRequestWithAnswersDTO requestDTO, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        ExerciseQuestion exerciseQuestion = exerciseQuestionMapper.exerciseQuestionRequestWithAnswersDTOToExerciseQuestion(requestDTO, exerciseID, userID);

        exerciseQuestion = exerciseQuestionRepository.save(exerciseQuestion);

        return exerciseQuestionMapper.exerciseQuestionToExerciseQuestionPublicWithAnswersDTO(exerciseQuestion);
    }

    @Override
    public List<ExerciseQuestionWithAnswersDTO> createMultipleQuestionsWithAnswers(Long exerciseID, List<ExerciseQuestionRequestWithAnswersDTO> requestDTOList, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionMapper.exerciseQuestionRequestWithAnswersDTOListToExerciseQuestionList(requestDTOList, exerciseID, userID);

        exerciseQuestionList = exerciseQuestionRepository.saveAll(exerciseQuestionList);

        return exerciseQuestionMapper.exerciseQuestionListToExerciseQuestionPublicWithAnswersDTOList(exerciseQuestionList);
    }

    @Override
    public List<ExerciseQuestionDTO> updateMultipleQuestions(List<ExerciseQuestionRequestWithIDContentOnlyDTO> requestDTOList, Long exerciseID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository
                .findAllById(requestDTOList.stream().map(ExerciseQuestionRequestWithIDContentOnlyDTO::getId).collect(Collectors.toList()));
        exerciseQuestionList = exerciseQuestionMapper.updateExerciseQuestionListFromExerciseQuestionRequestDTOList(requestDTOList, userID, exerciseQuestionList);
        exerciseQuestionList = exerciseQuestionRepository.saveAll(exerciseQuestionList);

        //Xử lý trường hợp tạo mới.
        //Tách ra vì trong khi tạo mới sẽ set thêm tác giả, ... nữa.
        //TODO: Kiểm tra rank không trùng nhau.
        List<ExerciseQuestionRequestDTO> newExerciseAnswerList = requestDTOList.stream()
                .filter(obj -> obj.getId() == null)
                .map(exerciseQuestionMapper::exerciseQuestionRequestDTOWithIDToExerciseQuestionRequestDTO)
                        .collect(Collectors.toList());
        List<ExerciseQuestionDTO> newExerciseAnswerSavedDTOList = this.createMultipleQuestions(exerciseID, newExerciseAnswerList, authentication);

        //Xoá đi những câu hỏi người dùng không put lên.
        List<ExerciseQuestion> deletedExerciseQuestion =
                exerciseQuestionRepository.findByIdNotIn(requestDTOList.stream().map(ExerciseQuestionRequestWithIDContentOnlyDTO::getId).collect(Collectors.toList()));
        exerciseQuestionRepository.deleteAll(deletedExerciseQuestion);

        List<ExerciseQuestionDTO> resultingList = exerciseQuestionMapper.exerciseQuestionListToExerciseQuestionPublicDTOList(exerciseQuestionList);
        resultingList.addAll(newExerciseAnswerSavedDTOList);

        return resultingList;
    }

    @Override
    public List<ExerciseQuestionWithAnswersDTO> updateMultipleQuestionsWithAnswers(List<ExerciseQuestionRequestWithIDAndAnswersWithIDsDTO> requestDTOList, Long exerciseID , Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository
                .findAllById(requestDTOList.stream().map(ExerciseQuestionRequestWithIDAndAnswersWithIDsDTO::getId).collect(Collectors.toList()));
        exerciseQuestionList = exerciseQuestionMapper
                .updateExerciseQuestionListFromExerciseQuestionRequestWithIDAndAnswersWithIDsDTOList(requestDTOList, userID, exerciseQuestionList);
        exerciseQuestionList = exerciseQuestionRepository.saveAll(exerciseQuestionList);

        //Xử lý trường hợp tạo mới.
        List<ExerciseQuestionRequestWithAnswersDTO> newExerciseQuestionList = requestDTOList.stream()
                        .filter(obj -> obj.getId() == null)
                        .map(exerciseQuestionMapper::exerciseQuestionRequestDTOWithIDToExerciseQuestionRequestDTO)
                        .collect(Collectors.toList());
        List<ExerciseQuestionWithAnswersDTO> newExerciseQuestionSavedList = this.createMultipleQuestionsWithAnswers(exerciseID, newExerciseQuestionList, authentication);

        //Xoá đi những câu hỏi người dùng không put lên.
        List<ExerciseQuestion> deletedExerciseQuestion =
                exerciseQuestionRepository.findByIdNotIn(requestDTOList.stream().map(ExerciseQuestionRequestWithIDAndAnswersWithIDsDTO::getId).collect(Collectors.toList()));
        exerciseQuestionRepository.deleteAll(deletedExerciseQuestion);

        List<ExerciseQuestionWithAnswersDTO> resultingList = exerciseQuestionMapper.exerciseQuestionListToExerciseQuestionPublicWithAnswersDTOList(exerciseQuestionList);
        resultingList.addAll(newExerciseQuestionSavedList);
        return resultingList;
    }

    @Override
    public void deleteQuestion(Long questionID, Authentication authentication) {
        exerciseQuestionRepository.deleteById(questionID);
    }

    @Override
    public void deleteMultipleQuestions (List<Long> questionIDs, Authentication authentication) {
        List<ExerciseQuestion> exerciseQuestionList = exerciseQuestionRepository.findAllById(questionIDs);

        exerciseQuestionRepository.deleteAll(exerciseQuestionList);
    }
}
