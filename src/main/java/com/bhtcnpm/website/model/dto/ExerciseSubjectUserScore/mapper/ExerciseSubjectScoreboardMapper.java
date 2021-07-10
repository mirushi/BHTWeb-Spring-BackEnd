package com.bhtcnpm.website.model.dto.ExerciseSubjectUserScore.mapper;

import com.bhtcnpm.website.model.dto.ExerciseSubjectUserScore.ExerciseSubjectScoreboardWithUserRankDTO;
import com.bhtcnpm.website.model.dto.ExerciseSubjectUserScore.ExerciseSubjectUserScoreDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubjectUserScore;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseSubjectUserScoreId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface ExerciseSubjectScoreboardMapper {
    @Mapping(target = "userID", source = "exerciseSubjectUserScore.exerciseSubjectUserScoreId.user.id")
    @Mapping(target = "userDisplayName", source = "exerciseSubjectUserScore.exerciseSubjectUserScoreId.user.displayName")
    @Mapping(target = "userAvatarURL", source = "exerciseSubjectUserScore.exerciseSubjectUserScoreId.user.avatarURL")
    ExerciseSubjectUserScoreDTO exerciseSubjectUserScoreToExerciseSubjectUserScoreDTO (ExerciseSubjectUserScore exerciseSubjectUserScore, Integer rank);

    default List<ExerciseSubjectUserScoreDTO> exerciseSubjectUserScoreListToExerciseSubjectUserScoreDTOList (List<ExerciseSubjectUserScore> exerciseSubjectUserScoreList) {
        List<ExerciseSubjectUserScoreDTO> result = new ArrayList<>();
        for (int i=0; i< exerciseSubjectUserScoreList.size(); ++i) {
            ExerciseSubjectUserScoreDTO dto = exerciseSubjectUserScoreToExerciseSubjectUserScoreDTO(exerciseSubjectUserScoreList.get(i), i + 1);
            result.add(dto);
        }
        return result;
    }

    default ExerciseSubjectScoreboardWithUserRankDTO createExerciseSubjectScoreboard (List<ExerciseSubjectUserScore> first10User, ExerciseSubjectUserScore userRankEntity, Integer userRank) {
        List<ExerciseSubjectUserScoreDTO> scoreBoard = exerciseSubjectUserScoreListToExerciseSubjectUserScoreDTOList(first10User);
        ExerciseSubjectUserScoreDTO userRankDTO = null;
        if (userRankEntity != null) {
            userRankDTO = exerciseSubjectUserScoreToExerciseSubjectUserScoreDTO(userRankEntity, userRank);
        }

        ExerciseSubjectScoreboardWithUserRankDTO result = new ExerciseSubjectScoreboardWithUserRankDTO();
        result.setExerciseScoreboard(scoreBoard);
        result.setUserRank(userRankDTO);

        return result;
    }

}
