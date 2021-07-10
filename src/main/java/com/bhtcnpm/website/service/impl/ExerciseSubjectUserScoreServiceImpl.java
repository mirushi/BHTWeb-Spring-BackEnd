package com.bhtcnpm.website.service.impl;

import com.bhtcnpm.website.model.dto.ExerciseSubjectUserScore.ExerciseSubjectScoreboardWithUserRankDTO;
import com.bhtcnpm.website.model.dto.ExerciseSubjectUserScore.ExerciseSubjectUserScoreDTO;
import com.bhtcnpm.website.model.dto.ExerciseSubjectUserScore.mapper.ExerciseSubjectScoreboardMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubjectUserScore;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubjectUserScoreId;
import com.bhtcnpm.website.repository.Exercise.ExerciseSubjectUserScoreRepository;
import com.bhtcnpm.website.repository.Subject.SubjectRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.ExerciseSubjectUserScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseSubjectUserScoreServiceImpl implements ExerciseSubjectUserScoreService {

    private final ExerciseSubjectUserScoreRepository exerciseSubjectUserScoreRepository;
    private final UserWebsiteRepository userWebsiteRepository;
    private final SubjectRepository subjectRepository;
    private final ExerciseSubjectScoreboardMapper exerciseSubjectScoreboardMapper;

    @Override
    public boolean addScore(UUID userID, Long subjectID, long score) {
        ExerciseSubjectUserScoreId id = new ExerciseSubjectUserScoreId();
        id.setUser(userWebsiteRepository.getOne(userID));
        id.setSubject(subjectRepository.getOne(subjectID));

        Optional<ExerciseSubjectUserScore> exerciseSubjectUserScore = exerciseSubjectUserScoreRepository.findById(id);
        ExerciseSubjectUserScore userScore;
        long previousTotalScore = 0;
        if (exerciseSubjectUserScore.isPresent()) {
            userScore = exerciseSubjectUserScore.get();
            previousTotalScore = userScore.getTotalScore();
        } else {
            userScore = new ExerciseSubjectUserScore();
            userScore.setExerciseSubjectUserScoreId(id);
        }
        userScore.setTotalScore(previousTotalScore + score);
        exerciseSubjectUserScoreRepository.save(userScore);

        return true;
    }

    @Override
    public ExerciseSubjectScoreboardWithUserRankDTO getScoreboard (Long subjectID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserID(authentication);

        //Lấy scoreboard.
        List<ExerciseSubjectUserScore> first10User = exerciseSubjectUserScoreRepository.findFirst10ByExerciseSubjectUserScoreIdSubjectIdOrderByTotalScoreDesc(subjectID);

        //Lấy rank của user.
        ExerciseSubjectUserScore userRankEntity = null;
        //TODO: Change integer to long.
        Integer userRank = null;
        if (userID != null) {
            ExerciseSubjectUserScoreId id = new ExerciseSubjectUserScoreId();
            id.setUser(userWebsiteRepository.getOne(userID));
            id.setSubject(subjectRepository.getOne(subjectID));
            Optional<ExerciseSubjectUserScore> exerciseSubjectUserScore = exerciseSubjectUserScoreRepository.findById(id);
            userRankEntity = exerciseSubjectUserScore.get();
        }

        //Mapper.
        ExerciseSubjectScoreboardWithUserRankDTO resultDTO = exerciseSubjectScoreboardMapper.createExerciseSubjectScoreboard(first10User, userRankEntity, userRank);

        return resultDTO;
    }

}
