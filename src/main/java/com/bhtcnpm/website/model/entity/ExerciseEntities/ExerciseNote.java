package com.bhtcnpm.website.model.entity.ExerciseEntities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "exercise_note")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseNote {
    @EmbeddedId
    private ExerciseNoteId exerciseNoteId;

    @Column(name = "notes")
    private String note;

    @Version
    private short version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExerciseNote)) return false;
        ExerciseNote other = (ExerciseNote) o;

        return Objects.equals(getExerciseNoteId(), other.getExerciseNoteId());
    }

    @Override
    public int hashCode() {return Objects.hash(getExerciseNoteId());}
}
