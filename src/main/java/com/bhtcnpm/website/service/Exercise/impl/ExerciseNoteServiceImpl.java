package com.bhtcnpm.website.service.Exercise.impl;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseNoteDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseNoteRequestDTO;
import com.bhtcnpm.website.model.dto.Exercise.mapper.ExerciseNoteMapper;
import com.bhtcnpm.website.model.entity.ExerciseEntities.Exercise;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseNote;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseNoteId;
import com.bhtcnpm.website.model.entity.UserWebsite;
import com.bhtcnpm.website.repository.Exercise.ExerciseNoteRepository;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import com.bhtcnpm.website.security.util.SecurityUtils;
import com.bhtcnpm.website.service.Exercise.ExerciseNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseNoteServiceImpl implements ExerciseNoteService {

    private final ExerciseRepository exerciseRepository;
    private final UserWebsiteRepository userWebsiteRepository;
    private final ExerciseNoteRepository exerciseNoteRepository;
    private final ExerciseNoteMapper exerciseNoteMapper;

    @Override
    public ExerciseNoteDTO getNoteByExerciseIDAndUser(Long exerciseID, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        Optional<ExerciseNote> exerciseNoteObject = exerciseNoteRepository.findByExerciseNoteIdExerciseIdAndExerciseNoteIdUserId(exerciseID, userID);

        if (exerciseNoteObject.isEmpty()) {
            return new ExerciseNoteDTO("");
        }

        ExerciseNote exerciseNoteEntity = exerciseNoteObject.get();

        return exerciseNoteMapper.exerciseNoteToExerciseNoteDTO(exerciseNoteEntity);
    }

    @Override
    public ExerciseNoteDTO putNoteByExerciseIDAndUser(Long exerciseID, ExerciseNoteRequestDTO requestDTO, Authentication authentication) {
        UUID userID = SecurityUtils.getUserIDOnNullThrowException(authentication);

        ExerciseNote exerciseNote = exerciseNoteMapper.exerciseNoteRequestDTOToExerciseNote(requestDTO, exerciseID, userID);
        exerciseNote = exerciseNoteRepository.save(exerciseNote);

        return exerciseNoteMapper.exerciseNoteToExerciseNoteDTO(exerciseNote);
    }
}
