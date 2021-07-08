package com.bhtcnpm.website.model.dto.Exercise.mapper;

import com.bhtcnpm.website.model.dto.Exercise.*;
import com.bhtcnpm.website.model.dto.Tag.TagMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.repository.Exercise.ExerciseCategoryRepository;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.repository.Exercise.ExerciseTopicRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mapper
public abstract class ExerciseMapper {

    protected ExerciseRepository exerciseRepository;
    protected ExerciseTopicRepository exerciseTopicRepository;
    protected ExerciseCategoryRepository exerciseCategoryRepository;
    protected UserWebsiteRepository userWebsiteRepository;
    protected TagMapper tagMapper;

    @Mapping(target = "id", source = "id")
    @Mapping(target = "bestCorrectQuestions", ignore = true)
    @Mapping(target = "note", ignore = true)
    public abstract ExerciseUserStatisticDTO exerciseIDToExerciseUserStatisticDTO (Long id);

    @Mapping(target = "attempted", constant = "false")
    @Mapping(target = "maxCorrectAnsweredQuestions", ignore = true)
    @Mapping(target = "totalQuestions", ignore = true)
    public abstract ExerciseSummaryDTO exerciseToExerciseSummaryDTO (Exercise exercise);

    @Mapping(target = "attempted", constant = "false")
    @Mapping(target = "maxCorrectAnsweredQuestions", ignore = true)
    @Mapping(target = "totalQuestions", ignore = true)
    @Mapping(target = "topicID", source = "topic.id")
    @Mapping(target = "topicName", source = "topic.name")
    public abstract ExerciseSummaryWithTopicDTO exerciseToExerciseSummaryWithTopicDTO (Exercise exercise);

    public abstract ExerciseSummaryDTO exerciseWithTopicToExerciseSummaryDTO(ExerciseSummaryWithTopicDTO exerciseSummaryWithTopicDTO);

    @Mapping(target = "authorID", source = "author.id")
    @Mapping(target = "authorAvatarURL", source = "author.avatarURL")
    @Mapping(target = "authorDisplayName", source = "author.displayName")
    @Mapping(target = "categoryID", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "topicID", source = "topic.id")
    @Mapping(target = "topicName", source = "topic.name")
    @Mapping(target = "subjectID", source = "topic.subject.id")
    @Mapping(target = "subjectName", source = "topic.subject.name")
    public abstract ExerciseDetailsDTO exerciseToExerciseDetailsDTO (Exercise exercise);

    public abstract List<ExerciseSummaryWithTopicDTO> exerciseIterableToExerciseSummaryWithTopicDTOList (Iterable<Exercise> exerciseIterable);

    public abstract List<ExerciseSummaryWithTopicDTO> exerciseListToExerciseSummaryWithTopicDTOList (List<Exercise> exerciseList);

    public abstract List<ExerciseSummaryDTO> exerciseIterableToExerciseSummaryDTOList (Iterable<Exercise> exerciseIterable);

    public abstract List<ExerciseSummaryDTO> exerciseListToExerciseSummaryDTOList (List<Exercise> exerciseList);

    public abstract List<ExerciseUserStatisticDTO> exerciseIDListToExerciseUserStatisticDTOList (List<Long> exerciseIDs);

    public Exercise updateExerciseFromExerciseRequestDTO (ExerciseRequestDTO exerciseRequestDTO, Exercise entity, UUID userID) {
        Exercise newExercise = Objects.requireNonNullElseGet(entity, Exercise::new);

        if (exerciseRequestDTO == null) {
            return entity;
        }

        if (entity == null) {
            newExercise.setSubmitDtm(LocalDateTime.now());
            newExercise.setPublishDtm(LocalDateTime.now());
            newExercise.setAuthor(userWebsiteRepository.getOne(userID));
            newExercise.setVersion((short)0);
        }

        newExercise.setTitle(exerciseRequestDTO.getTitle());
        newExercise.setDescription(exerciseRequestDTO.getDescription());
        newExercise.setSuggestedDuration(exerciseRequestDTO.getSuggestedDuration());
        newExercise.setPublishDtm(exerciseRequestDTO.getPublishDtm());
        newExercise.setRank(exerciseRequestDTO.getRank());
        newExercise.setTopic(exerciseTopicRepository.getOne(exerciseRequestDTO.getTopicID()));
        newExercise.setCategory(exerciseCategoryRepository.getOne(exerciseRequestDTO.getCategoryID()));
        newExercise.setTags(tagMapper.tagDTOListToTagList(exerciseRequestDTO.getTags()));

        return newExercise;
    }

    @Autowired
    public void setExerciseRepository (ExerciseRepository exerciseRepository) { this.exerciseRepository = exerciseRepository; }

    @Autowired
    public void setUserWebsiteRepository (UserWebsiteRepository userWebsiteRepository) { this.userWebsiteRepository = userWebsiteRepository; }

    @Autowired
    public void setExerciseTopicRepository (ExerciseTopicRepository exerciseTopicRepository) { this.exerciseTopicRepository = exerciseTopicRepository; }

    @Autowired
    public void setExerciseCategoryRepository (ExerciseCategoryRepository exerciseCategoryRepository) { this.exerciseCategoryRepository = exerciseCategoryRepository; }

    @Autowired
    public void setTagMapper (TagMapper tagMapper) { this.tagMapper = tagMapper; }
}
