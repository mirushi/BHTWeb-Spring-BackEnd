package com.bhtcnpm.website.model.dto.Exercise.mapper;

import com.bhtcnpm.website.model.dto.Exercise.ExerciseNoteDTO;
import com.bhtcnpm.website.model.dto.Exercise.ExerciseNoteRequestDTO;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseNote;
import com.bhtcnpm.website.model.entity.ExerciseEntities.ExerciseNoteId;
import com.bhtcnpm.website.repository.Exercise.ExerciseRepository;
import com.bhtcnpm.website.repository.UserWebsiteRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper
public abstract class ExerciseNoteMapper {

    protected ExerciseRepository exerciseRepository;
    protected UserWebsiteRepository userWebsiteRepository;

    public abstract ExerciseNoteDTO exerciseNoteToExerciseNoteDTO (ExerciseNote exerciseNote);

    public ExerciseNote exerciseNoteRequestDTOToExerciseNote (ExerciseNoteRequestDTO exerciseNoteRequestDTO, Long exerciseID, UUID userID) {
        ExerciseNoteId exerciseNoteId = new ExerciseNoteId(exerciseRepository.getOne(exerciseID), userWebsiteRepository.getOne(userID));
        ExerciseNote exerciseNote = new ExerciseNote();
        exerciseNote.setExerciseNoteId(exerciseNoteId);
        exerciseNote.setNote(exerciseNoteRequestDTO.getNote());
        exerciseNote.setVersion((short)0);

        return exerciseNote;
    }

    @Autowired
    public void setExerciseRepository (ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @Autowired
    public void setUserWebsiteRepository (UserWebsiteRepository userWebsiteRepository) {
        this.userWebsiteRepository = userWebsiteRepository;
    }
}
